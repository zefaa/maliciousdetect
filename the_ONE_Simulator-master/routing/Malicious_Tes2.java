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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author M S I
 */
public class Malicious_Tes2 implements RoutingDecisionEngineMalicious {

    protected LinkedList<Double> resourcesList;
    private int interval;
    public static final String TOTAL_CONTACT_INTERVAL = "perTotalContact";
    public static final int DEFAULT_CONTACT_INTERVAL = 300;
    private Double lastRecord = Double.MIN_VALUE;

    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>();
    private List<Message> rcvMsg;

    public Malicious_Tes2(Settings s) {
        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
        } else {
            interval = DEFAULT_CONTACT_INTERVAL;
        }
    }

    public Malicious_Tes2(Malicious_Tes2 proto) {
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
        if (thisHost.toString().startsWith("mal")) {
            ArrayList<Message> temp = new ArrayList<Message>(thisHost.getMessageCollection());

            for (Message message : temp) {

                if (thisHost != message.getFrom() && thisHost != message.getTo()) {
//                        thisHost.deleteMessage(message.getId(), true);
                    Random rand = new Random();
                    int rng = rand.nextInt(2);
                    switch (rng) {
                        case 0:
                            break;
                        case 1:
                            thisHost.deleteMessage(message.getId(), true);
                            System.out.println("this ho : " + thisHost + " m : " + message);
                            break;
                    }
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

//        rcvMsg = saveMsg.get(from);
//        rcvMsg.add(m);
        return !thisHost.getRouter().hasMessage(m.getId());

    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost, DTNHost thisHost) {
        return true;
    }

    @Override
    public boolean shouldDeleteSentMessage(Message m, DTNHost otherHost) {
//        System.out.println("oth : "+otherHost +" m : "+m);
        return true;
    }

    @Override
    public boolean shouldDeleteOldMessage(Message m, DTNHost hostReportingOld) {
        return true;
    }

    @Override
    public void update(DTNHost thisHost) {

    }

    @Override
    public RoutingDecisionEngineMalicious replicate() {
        return new Malicious_Tes2(this);
    }

}
