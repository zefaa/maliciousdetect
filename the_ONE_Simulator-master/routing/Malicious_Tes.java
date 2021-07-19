/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routing;

import core.Connection;
import core.DTNHost;
import core.Message;
import core.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author M S I
 */
public class Malicious_Tes implements RoutingDecisionEngineMalicious {

    protected LinkedList<Double> resourcesList;
    private int interval;
    public static final String TOTAL_CONTACT_INTERVAL = "perTotalContact";
    public static final int DEFAULT_CONTACT_INTERVAL = 300;
    private Double lastRecord = Double.MIN_VALUE;

    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>();
    private List<Message> rcvMsg;

    public Malicious_Tes(Settings s) {
        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
        } else {
            interval = DEFAULT_CONTACT_INTERVAL;
        }
    }

    public Malicious_Tes(Malicious_Tes proto) {
        resourcesList = new LinkedList<>();
        interval = proto.interval;
        lastRecord = proto.lastRecord;
    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {
        List<Message> pesan = new ArrayList<Message>();

        saveMsg.put(peer, pesan);
    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {
        List<Message> rcvMsg = saveMsg.get(peer);

            for (Message readRcvMsg : rcvMsg) {

                if (thisHost.toString().startsWith("mal")) {

                    if (thisHost != readRcvMsg.getFrom() && thisHost != readRcvMsg.getTo()) {

                        thisHost.deleteMessage(readRcvMsg.getId(), true);

                    }
                }
        }

    }

    @Override
    public void doExchangeForNewConnection(Connection con, DTNHost peer) {

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
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost, DTNHost from) {

        rcvMsg = saveMsg.get(from);
        rcvMsg.add(m);

        return true;
    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost, DTNHost thisHost) {
        return true;
    }

    @Override
    public boolean shouldDeleteSentMessage(Message m, DTNHost otherHost) {

        return true;
    }

    @Override
    public boolean shouldDeleteOldMessage(Message m, DTNHost hostReportingOld) {
        return false;
    }

    @Override
    public void update(DTNHost thisHost) {

    }

    @Override
    public RoutingDecisionEngineMalicious replicate() {
        return new Malicious_Tes(this);
    }

    
    
}
