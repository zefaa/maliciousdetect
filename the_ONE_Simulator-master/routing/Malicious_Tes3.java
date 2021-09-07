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
import core.Tuple;
import java.util.*;

/**
 *
 * @author M S I
 */
public class Malicious_Tes3 implements RoutingDecisionEngineMalicious {

    protected LinkedList<Double> resourcesList;
    private int interval;
    public static final String TOTAL_CONTACT_INTERVAL = "perTotalContact";
    public static final int DEFAULT_CONTACT_INTERVAL = 300;

    /**
     * identifier for the initial number of copies setting ({@value})
     */
    /**
     * Message property key for the remaining available copies of a message
     */
    private Double lastRecord = Double.MIN_VALUE;

    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>();
//    private Map<DTNHost, List<Message>> dropMsg = new HashMap<>();
    private List<Message> rcvMsg;
    private Map<DTNHost, Double> trustValueNode;
    Double trust;
    private List<String> cekMsgRcv;
    private List<String> cekMsgSend;
    Set<DTNHost> blackList = new HashSet<>();
    String hash;
    List<String> mHash;
    List<String> pesan;
    DTNHost myHost;

    public Malicious_Tes3(Settings s) {
//        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
//            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
//        } else {
//            interval = DEFAULT_CONTACT_INTERVAL;
//        }
        trustValueNode = new HashMap<DTNHost, Double>();

    }

    public Malicious_Tes3(Malicious_Tes3 proto) {
//        resourcesList = new LinkedList<>();
//        interval = proto.interval;
//        lastRecord = proto.lastRecord;
        trustValueNode = new HashMap<DTNHost, Double>();

    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {
        List<Message> pesan = new ArrayList<Message>();

        saveMsg.put(peer, pesan);

//        trust = thisHost.getTrustValue(peer);
//
//        trustValueNode.put(peer, trust);
//
//        //node yang nilai trustValuenya <= 0.1 masukkan ke blackList.
//        for (Map.Entry<DTNHost, Double> entry : trustValueNode.entrySet()) {
//            DTNHost trustKey = entry.getKey();
//            Double trustValue = entry.getValue();
//
//            if (entry.getValue() <= 0.1) {
//                blackList.add(peer);
//                System.out.println("black list : "+blackList);
//            }
//        }
    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {
//        List<Message> drMsg = new ArrayList<>();

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

//        List<Message> rcvMsg = saveMsg.get(peer);
//        Collection <Message> col = thisHost.getMessageCollection();
//        try {
//           Message messages = (Message) thisHost.getMessageCollection();
//          
//            for (Message message : thisHost.getMessageCollection()) {
//                if (thisHost.toString().startsWith("mal")) {
//                             //if (thisHost != messages.getFrom() && thisHost != messages.getTo()) {
// thisHost.deleteMessage(message.getId(), true);
//            }
//                        //}
// }
//        } catch (Exception e) {
//        }
//        try {
//            for (Message readRcvMsg : rcvMsg) {
//                if (thisHost.toString().startsWith("mal")) {
//                    if (thisHost != readRcvMsg.getFrom() && thisHost != readRcvMsg.getTo()) {
//                        Random rng = new Random();
//                        int rand = rng.nextInt(2);
//
//                        if (rand == 0) {
//                            thisHost.deleteMessage(readRcvMsg.getId(), true);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
    }

    @Override
    public void doExchangeForNewConnection(Connection con, DTNHost peer) {
//        myHost = con.getOtherNode(peer);
    }

    @Override
    public boolean newMessage(Message m) {

        return true;
    }

    @Override
    public boolean isFinalDest(Message m, DTNHost aHost
    ) {
        return m.getTo() == aHost;
    }

    @Override
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost, DTNHost from) {

        rcvMsg = saveMsg.get(from);
        rcvMsg.add(m);

        Map<String, List<String>> grupRcv = new HashMap<>(); //grup pesan yang diterima
        Map<String, List<String>> grupSend = new HashMap<>(); //grup pesan sesuai tuple

        try {
            for (Message msg : rcvMsg) {
                Tuple<List<String>, String> getTuple = (Tuple<List<String>, String>) msg.getProperty("hashEmpat");
                hash = getTuple.getValue();
                mHash = getTuple.getKey();

                if (!grupRcv.containsKey(hash)) {
                    pesan = new ArrayList<String>();

                } else {
                    pesan = grupRcv.get(hash);

                }
                pesan.add(msg.toString());
                grupRcv.put(hash, pesan);
                

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
//                cekMsgSend = grupSend.get(hash);
//
//            }
                //System.out.println("ck sen : "+cekMsgSend);
//            if (cekMsgSend != cekMsgRcv) {
//                trust = trust - 0.1;
                trustValueNode.put(from, trust);
//
//                return false;
            }
            }catch (Exception e) {
        }
            return m.getTo() != thisHost;
        }

        @Override
        public boolean shouldSendMessageToHost
        (Message m, DTNHost otherHost
        ,
            DTNHost thisHost
        
            ) {
        return true;
        }

        @Override
        public boolean shouldDeleteSentMessage
        (Message m, DTNHost otherHost
        
            ) {

        return true;
        }

        @Override
        public boolean shouldDeleteOldMessage
        (Message m, DTNHost hostReportingOld
        
            ) {
        return false;
        }

        @Override
        public void update
        (DTNHost thisHost
        
        
        ) {

    }

    @Override
        public RoutingDecisionEngineMalicious replicate
        
            () {
        return new Malicious_Tes3(this);
        }

    }
