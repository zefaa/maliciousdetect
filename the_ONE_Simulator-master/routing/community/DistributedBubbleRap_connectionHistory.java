/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routing.community;

import core.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import routing.DecisionEngineRouter;
import routing.MessageRouter;
import routing.RoutingDecisionEngine;

/**
 *
 * @author M S I
 */
public class DistributedBubbleRap_connectionHistory implements RoutingDecisionEngine,help {

 protected Map<DTNHost, Double> startTimestamps;
    protected Map<DTNHost, List<Duration>> connHistory;

    private double total = 0;

    public DistributedBubbleRap_connectionHistory(Settings s) {
        startTimestamps = new HashMap<DTNHost, Double>();
        connHistory = new HashMap<DTNHost, List<Duration>>();
    }

    public DistributedBubbleRap_connectionHistory(DistributedBubbleRap_connectionHistory proto) {
        startTimestamps = new HashMap<DTNHost, Double>();
        connHistory = new HashMap<DTNHost, List<Duration>>();
    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {
    }

    @Override
    public void doExchangeForNewConnection(Connection con, DTNHost peer) {
        DTNHost myHost = con.getOtherNode(peer);
       DistributedBubbleRap_connectionHistory de = this.getOtherDecisionEngine(peer);

        this.startTimestamps.put(peer, SimClock.getTime());
        de.startTimestamps.put(peer, SimClock.getTime());
    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {
        double time = 0;
        if (startTimestamps.get(peer) != null) {
            time = startTimestamps.get(peer);
        }

        double etime = SimClock.getTime();

        List<Duration> history;

        if (!connHistory.containsKey(peer)) {
            history = new LinkedList<Duration>();
            connHistory.put(peer, history);
        } else {
            history = connHistory.get(peer);
        }

        if (etime - time > 0) {
            history.add(new Duration(time, etime));
        }

        startTimestamps.remove(peer);

    }

    @Override
    public boolean newMessage(Message m) {
        return true;
    }

    @Override
    public boolean isFinalDest(Message m, DTNHost aHost) {
        return m.getTo() == aHost;
    }

    @Override
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost) {
        return m.getTo() != thisHost;
    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost, DTNHost thisHost) {
        //intra-berapa lama kita bertemu
//        if (m.getTo() == otherHost) {
//            return true;
//        }
//       
//        DTNHost dest = m.getTo();
//        DistributedBubbleRap_connectionHistory de = getOtherDecisionEngine(otherHost);
//       
//        double peer = de.getConn(dest); //peer ke dest
//        double me = this.getConn(dest); //me ke dest
//       
//        if (peer > me){
//            return true;
//        }else{
//            return false;
//        }

        //inter-berapa lama kita ga ketemu
        if (m.getTo() == otherHost) {
            return true;
        }

        DTNHost dest = m.getTo();
        DistributedBubbleRap_connectionHistory de = getOtherDecisionEngine(otherHost);

        double peer = de.getConn(dest); //peer ke dest
        double me = this.getConn(dest); //me ke dest

        if (peer < me) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldDeleteSentMessage(Message m, DTNHost otherHost) {
        return false;
    }

    @Override
    public boolean shouldDeleteOldMessage(Message m, DTNHost hostReportingOld) {
        return false;
    }

    @Override
    public void update(DTNHost thisHost) {
    }

    @Override
    public RoutingDecisionEngine replicate() {
        return new DistributedBubbleRap_connectionHistory(this);
    }

    private DistributedBubbleRap_connectionHistory getOtherDecisionEngine(DTNHost h) {
        MessageRouter otherRouter = h.getRouter();
        assert otherRouter instanceof DecisionEngineRouter : "This router only works "
                + " with other routers of same type";

        return (DistributedBubbleRap_connectionHistory) ((DecisionEngineRouter) otherRouter).getDecisionEngine();
    }

    private double getConn(DTNHost h) {
        double jumlah = 0;
        List<Duration> history = new LinkedList<>();
        if (connHistory.containsKey(h)) {
            history = connHistory.get(h);
        }
        for (Iterator<Duration> iterator = history.iterator(); iterator.hasNext();) {
            Duration next = iterator.next();
            total = total + (next.end - next.start);

            jumlah++;
        }

        return total / jumlah;
    }

    private double getInterConn(DTNHost h) {
        List<Duration> history = new LinkedList();
        if (connHistory.containsKey(h)) {
            history = connHistory.get(h);
        }
        double total = 0;
        double sumNode = history.size();

        if (sumNode == 1) {
            return 0;
        }

        Double end = new Double(0);
        for (Iterator<Duration> iterator = history.iterator(); iterator.hasNext();) {
            Duration next = iterator.next();
            if (end == 0) {
                end = next.start;
            }
            total = total + (next.start - end);
            end = next.end; //--

        }

        return total / (sumNode - 1);
    }

    @Override
    public double Rerata() {
        double sum = 0;
        double count = 0;
        double rerata = 0;

        for (Map.Entry<DTNHost, List<Duration>> entry : connHistory.entrySet()) {
            DTNHost hos = entry.getKey();
            sum = sum + getInterConn(hos);

        }
        count = connHistory.size();
        rerata = sum / count;
        return rerata;
    }

    

}
