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
import core.SimClock;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Afra Rian
 */
public class EpidemicTestMal implements RoutingDecisionEngineMalicious, NodeMaliciousHelper {

    /**
     * For Report purpose, maybe needed some variable
     */
    protected LinkedList<Double> resourcesList;
    public static final String TOTAL_CONTACT_INTERVAL = "perTotalContact";
    public static final int DEFAULT_CONTACT_INTERVAL = 300;
    private Double lastRecord = Double.MIN_VALUE;
    private int interval;

    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>();
    private LinkedList drop = new LinkedList();

    public EpidemicTestMal(Settings s) {
        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
        } else {
            interval = DEFAULT_CONTACT_INTERVAL;
        }
    }

    public EpidemicTestMal(EpidemicTestMal proto) {
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
        List<Message> psn = saveMsg.get(peer);

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
        List<Message> psn = saveMsg.get(from);

        if (thisHost.toString().startsWith("mal")) {
            Random rng = new Random();
            int rand = 1 * rng.nextInt() + 0;

            switch (rand) {
                case 0:
                    return m.getTo() != thisHost;
                default:
                    drop.add(m);
                    return false;
            }

        }
        return !thisHost.getRouter().hasMessage(m.getId());
    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost, DTNHost thisHost) {

        return true;
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
    public RoutingDecisionEngineMalicious replicate() {
        return new EpidemicTestMal(this);
    }

    @Override
    public void update(DTNHost thisHost) {
    }

    @Override
    public LinkedList<Message> getNodeMalicious() {
        return drop;
    }

}
