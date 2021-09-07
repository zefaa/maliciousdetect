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
import core.SimScenario;
import core.Tuple;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
public class Malicious_Tes implements RoutingDecisionEngineMalicious {

    protected LinkedList<Double> resourcesList;
    private int interval;
    public static final String TOTAL_CONTACT_INTERVAL = "perTotalContact";
    public static final int DEFAULT_CONTACT_INTERVAL = 300;
    private Double lastRecord = Double.MIN_VALUE;

    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>();
    private List<Message> rcvMsg;
    List<Message> pesanProp;
    String hash;

    private List<String> cekMsgRcv;
    private List<String> cekMsgSend;
    List<DTNHost> blackList;
    Double trust = 0.5;
    private Map<DTNHost, Double> trustValueNode;

    public Malicious_Tes(Settings s) {
        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
        } else {
            interval = DEFAULT_CONTACT_INTERVAL;
        }

        blackList = new ArrayList<>();
        trustValueNode = new HashMap<>();
    }

    public Malicious_Tes(Malicious_Tes proto) {
        resourcesList = new LinkedList<>();
        interval = proto.interval;
        lastRecord = proto.lastRecord;
        blackList = new ArrayList<>();
        trustValueNode = new HashMap<>();
    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {
        List<Message> pesan = new ArrayList<Message>();
        List<DTNHost> listHo = (List<DTNHost>) SimScenario.getInstance().getHosts();
        saveMsg.put(peer, pesan);

        if (trustValueNode.isEmpty()) {
            for (DTNHost dTNHost : listHo) {
                trustValueNode.put(dTNHost, trust);
            }
        } else {
            Double cekTrustVal = trustValueNode.get(peer);
            if (cekTrustVal <= 0.1) {
                blackList.add(peer);
                System.out.println("ck2 " + blackList);
            }
        }
    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer
    ) {
        if (thisHost.toString().startsWith("mal")) {
            ArrayList<Message> temp = new ArrayList<Message>(thisHost.getMessageCollection());

            for (Message message : temp) {

                if (thisHost != message.getFrom() && thisHost != message.getTo()) {
                    //    thisHost.deleteMessage(readRcvMsg.getId(), true);
                    Random rand = new Random();
                    int rng = rand.nextInt(2);
                    switch (rng) {
                        case 0:
                            break;
                        case 1:
                            //System.out.println("this ho : "+ thisHost + " m : "+message);
                            thisHost.deleteMessage(message.getId(), true);
                            break;
                    }
                }

            }

        }
    }

    @Override
    public void doExchangeForNewConnection(Connection con, DTNHost peer
    ) {

    }

    @Override
    public boolean newMessage(Message m
    ) {
        return true;
    }

    @Override
    public boolean isFinalDest(Message m, DTNHost aHost
    ) {
        return m.getTo() == aHost;
    }

    @Override
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost,
            DTNHost from
    ) {
        rcvMsg = saveMsg.get(from);
        rcvMsg.add(m);
        System.out.println("ck " + rcvMsg);
        Map<String, List<String>> grupRcv = new HashMap<>(); //grup pesan yang diterima
        Map<String, List<Message>> grupSend = new HashMap<>(); //grup pesan sesuai tuple

        List<String> mTuple;
        try {
            for (Message msg : rcvMsg) {
                Tuple<List<String>, String> getTuple = (Tuple<List<String>, String>) msg.getProperty("hashEmpat");
                hash = getTuple.getValue();

                if (!grupSend.containsKey(hash)) {
                    pesanProp = new ArrayList<Message>();

                } else {
                    pesanProp = grupSend.get(hash);

                }
                pesanProp.add(msg);
                grupSend.put(hash, pesanProp);
                System.out.println("gr "+grupSend);
            }

//            for (Map.Entry<String, List<Message>> readGrup : grupSend.entrySet()) {
//                String readKey = readGrup.getKey();
//                List<Message> readValue = readGrup.getValue();
//                List<String> msgCek = new ArrayList<>();
//
//                for (Message cekValue : readValue) {
//                    msgCek.add(cekValue.toString());
//                    List<String> hash = input.MerkleTree.getNewMsgList_4(msgCek);
//                    System.out.println("ck3 "+readKey);
//                    System.out.println("ck4 "+hash);
//                }
//            }

//                if (!grupRcv.containsKey(hash)) {
//                    pesan = new ArrayList<String>();
//
//                } else {
//                    pesan = grupRcv.get(hash);
//
//                }
//                pesan.add(msg.toString());
//                grupRcv.put(hash, pesan);
//
//                grupSend.put(hash, mHash);
//
//                cekMsgRcv = new ArrayList<>(grupRcv.get(hash));
//
//                Collections.sort(cekMsgRcv, new Comparator<String>() {
//                    @Override
//                    public int compare(String t, String t1) {
//                        return new Integer(t.replaceAll("M", ""))
//                                .compareTo(new Integer(t1.replaceAll("M", "")));
//                    }
//                });
//
//                cekMsgSend = grupSend.get(hash);
//                //System.out.println("pes " + cekMsgSend);
//            }
//
//            if (cekMsgSend != cekMsgRcv) {
//                trust = from.getTrustValue() - 0.1;
//
//            } else {
//                trust = from.getTrustValue() + 0.1;
        } catch (Exception e) {
        }
//        System.out.println("tr " + trustValueNode);
        return m.getTo() != thisHost;
    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost,
            DTNHost thisHost
    ) {
//        if (malicousNode(otherHost)) {
//             System.out.println(trustValueNode);
//        }

        return true;
    }

    @Override
    public boolean shouldDeleteSentMessage(Message m, DTNHost otherHost
    ) {
//        System.out.println("oth : "+otherHost +" m : "+m);
        return true;
    }

    @Override
    public boolean shouldDeleteOldMessage(Message m, DTNHost hostReportingOld
    ) {
        return false;
    }

    @Override
    public void update(DTNHost thisHost
    ) {

    }

    @Override
    public RoutingDecisionEngineMalicious replicate() {
        return new Malicious_Tes(this);
    }

    private boolean malicousNode(DTNHost otherHost) {
        if (trust <= 0.1) {

            return true;
        }
        return false;
    }
}
