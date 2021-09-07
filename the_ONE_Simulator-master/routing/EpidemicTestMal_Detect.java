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
public class EpidemicTestMal_Detect implements RoutingDecisionEngineMalicious, NodeMaliciousHelper {

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
    double trustVal;
    private Map<DTNHost, List<Message>> saveMsgThis = new HashMap<>();
    
    Map<String, Map<List<String>, List<Message>>> hashMesg = new HashMap<>();
    
    List<String> hashMsg = new ArrayList<>();
    ArrayList<Integer> valList;
    int trysize = 0;
    List<DTNHost> maliciousList = new ArrayList<DTNHost>();
    List<DTNHost> normalNode = new ArrayList<DTNHost>();
    List<Message> pesan;
    int i = 0;
    
    public EpidemicTestMal_Detect(Settings s) {
        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
        } else {
            interval = DEFAULT_CONTACT_INTERVAL;
        }
    }
    
    public EpidemicTestMal_Detect(EpidemicTestMal_Detect proto) {
        resourcesList = new LinkedList<>();
        interval = proto.interval;
        lastRecord = proto.lastRecord;
        pesan = new ArrayList<Message>();
        
    }
    
    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {

        //trustVal = thisHost.getTrustValue(peer);
        //saveMsg.put(peer, pesan);
        //saveMsgThis.put(thisHost, pesan);
    }
    
    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {
//        
//        List<Message> psn = saveMsg.get(peer);
//        int as = 1;
//        for (Message msg : psn) {
//            if (msg != null) {
//                if (thisHost.toString().startsWith("mal")) {
//                    if (thisHost != msg.getFrom() && thisHost != msg.getTo()) {
//                        thisHost.deleteMessage(msg.toString(), true);
//                    }
//                }
//
//            }
//
//        }

 List<Message> rcvMsg = saveMsg.get(peer);

        try {
            for (Message readRcvMsg : rcvMsg) {
                if (thisHost.toString().startsWith("mal")) {
                    if (thisHost != readRcvMsg.getFrom() && thisHost != readRcvMsg.getTo()) {
                        Random rng = new Random();
//                        int rand = rng.nextInt(2);
//
//                        if (rand == 0) {
                            thisHost.deleteMessage(readRcvMsg.toString(), true);
//                        }
                    }
                }
            }
        } catch (Exception e) {
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
//        
//        List<Message> psn = saveMsg.get(from);
//
//        if (thisHost.toString().startsWith("mal")) {
//            Random rng = new Random();
//            int rand = 1 * rng.nextInt() + 0;
//
//            switch (rand) {
//                case 0:
//                    return m.getTo() != thisHost;
//                default:
//                    drop.add(m);
//                    return false;
//            }
//
//        }
//        return !thisHost.getRouter().hasMessage(m.getId());
        
//        if (thisHost.toString().startsWith("mal")) {
//            Random rng = new Random();
//            int rand = 1 * rng.nextInt() + 0;
//            
//            if (m != null) {
//                System.out.println(m);
//                if (i < 4) {
//                thisHost.deleteMessage(m.getId(), false);
//                i++;
//                return false;
//            } else {
//                return true;
//            }
//            }
//            
//        }
//        if (i < 8) {
//            pesan.add(m);
//            saveMsg.put(from, pesan);
//            i++;
//        }
//        if (i == 7) {
//            List<Message> psn = saveMsg.get(from);
//            if (thisHost.toString().startsWith("mal")) {
//                Random rng = new Random();
//                int rand = 1 * rng.nextInt() + 0;
//                System.out.println(rand);
//                
//                switch (rand) {
//                    case 0:
//                        thisHost.deleteMessage(psn.toString(), false);
//                        return false;
////                    return m.getTo() != thisHost;
//                    default:
//                        //System.out.println(psn);
//                        return true;
//
////            }
//                }
//
//            }
//        }
        //Map<Map<String, Map<List<String>, List<Message>>>, List<Message>> sendMsgGrup = new HashMap<>();
//        System.out.println("m : "+psn);
//        if (thisHost.toString().startsWith("mal")) {
//            Random rng = new Random();
//            int rand = 1 * rng.nextInt() + 0;
//
//            switch (rand) {
//                case 0:
//                    thisHost.deleteMessage(m.toString(), true);
////                    return m.getTo() != thisHost;
//                default:
//                    return true;
//
////            }
//        }
//        try {
//            /**
//             * gruping pesan yang masuk berdasarkan hash yang ada di properti
//             * pesan.
//             */
//            psn.add(m);
//            for (Message message : psn) {
//                hashMesg = (Map<String, Map<List<String>, List<Message>>>) message.getProperty("hashSatu");
//
//                if (!sendMsgGrup.containsKey(hashMesg)) {
//                    pesan = new ArrayList<Message>();
//
//                } else {
//                    pesan = sendMsgGrup.get(hashMesg);
//                }
//
//                pesan.add(message);
//
//                sendMsgGrup.put(hashMesg, pesan);
//
//                //System.out.println("h : "+sendMsgGrup); 
//                for (Map<List<String>, List<Message>> value : hashMesg.values()) {
//                    System.out.println("val");
//                }
//            }
//
//        } catch (Exception e) {
//        }

      return !thisHost.getRouter().hasMessage(m.getId());
    }
    
    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost, DTNHost thisHost) {

        int mSize = m.getSize();
        int bOth = otherHost.getRouter().getFreeBufferSize();
        
        int buf = bOth / mSize;
        List<Message> shSend = saveMsgThis.get(thisHost);
        List<Message> psn = saveMsg.get(otherHost);
        
        try {
            ArrayList<Integer> clMethod = new ArrayList<Integer>(this.shouldSendMessageBuffer(shSend));
            int clMethodSize = clMethod.size();

//            for (DTNHost malNode : maliciousList) {
//                if (otherHost.equals(malNode)) {
//                    return false;
//                } else {
            if (clMethodSize < buf) {
                return true;
            }
//                }
//            }

        } catch (Exception e) {
        }
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
    public RoutingDecisionEngineMalicious replicate() {
        return new EpidemicTestMal_Detect(this);
    }
    
    @Override
    public void update(DTNHost thisHost) {
    }
    
    @Override
    public LinkedList<Message> getNodeMalicious() {
        return drop;
    }
    
    

    /**
     * pengiriman pesan berdasarkan kondisi buffer
     *
     * @param sendMsg
     * @return
     */
    
    public int shouldSendMessageBuffer(List<Message> sendMsg) {
        //gruping pesan
        Map<List<String>, List<Message>> sendMsgGrup = new HashMap<>();
        List<Message> pesanTemp = new ArrayList<>();
        try {
            for (Message message : sendMsg) {
                hashMsg = (List<String>) message.getProperty("hashEmpat");
                if (!sendMsgGrup.containsKey(hashMsg)) {
                    pesan = new ArrayList<Message>();
                    
                } else {
                    pesan = sendMsgGrup.get(hashMsg);
                }
                pesan.add(message);
                sendMsgGrup.put(hashMsg, pesan);
            }

            //hitung total size dari pesan yang akan dikirim
            pesanTemp = sendMsgGrup.get(hashMsg);
            
            Map<List<Message>, Integer> conMsg = new HashMap<>();
            
            conMsg.put(pesanTemp, pesanTemp.size());
            trysize = pesanTemp.size();

            //valList = new ArrayList<Integer>(conMsg.values());
            //int valSize = valList.size();
//            System.out.println("gr "+valSize + "psn temp "+pesanTemp.size());
        } catch (Exception e) {
        }
        return pesanTemp.size();
    }
    
    
    //method verifikasi
//    public void verifikasiPesan(List<Message> psn) {
//        //gruping pesan
//        Map<List<String>, List<Message>> grupPesan = new HashMap<>(); //simpan pesan ke dalam grup pake Map, key : properti hash; value : id pesan
//
//        try {
//
//            for (Message m : psn) { //baca isi list psn satu persatu
//                hashMsg = (List<String>) m.getProperty("hash"); //ambil property hash yang ada di tiap pesan
//
//                if (!grupPesan.containsKey(hashMsg)) { //cek tidak ada grup pesan dengan hash tertentu
//                    pesan = new ArrayList<Message>(); // buat arraylist untuk menampung pesan dalam grup dengan hash yang baru
//                } else {
//                    pesan = grupPesan.get(hashMsg);    //jika ada, get hash dari grup pesan
//                }
//
//                pesan.add(m); //tambahkan message dalam pesan
//
//                grupPesan.put(hashMsg, pesan); //tambahkan hash dan id pesan dalam grup pesan
//
//            }
//            for (Map.Entry<List<String>, List<Message>> entry : grupPesan.entrySet()) {
//                List<String> key = entry.getKey();
//                List<Message> value = entry.getValue();
//
////                System.out.println("val : " + value);
//                List<Message> temp = value;
////                for (int i = 0; i < temp.size() - 1; i++) {
////                    for (int j = i + 1; j < temp.size() - j; j++) {
//
//                for (int i = 0; i < temp.size() - 1; i++) {
//                    for (int j = i + 1; j < temp.size(); j++) {
//                        String str1 = temp.get(i).toString();
//                        String str2 = temp.get(j).toString();
//                        int m1 = str1.toCharArray().length;
//                        int s1 = Integer.parseInt(str1.substring(1, m1));
//                        int m2 = str2.toCharArray().length;
//                        int s2 = Integer.parseInt(str2.substring(1, m2));
////                        int st = Integer.parseInt(str.toCharArray());
////                        System.out.println(j);
////                        System.out.println(temp.get(i) + " icreat : " + s1);
////                        System.out.println(temp.get(j) + " jcreate : " + s2);
//
//                        if (s1 > s2) {
//                            Message m = temp.get(i);
//                            temp.set(i, temp.get(j));
//                            temp.set(j, m);
//                        }
//
//                    }
//                }
////                System.out.println("temp " + temp);
//                grupPesan.replace(key, temp);
//            }
//
////                System.out.println("val2 : " + value);
//            for (Map.Entry<List<String>, List<Message>> entry : grupPesan.entrySet()) {
//                List<String> key = entry.getKey();
//                List<Message> value = entry.getValue();
//                List<String> msgCek = new ArrayList<>();
//
//                for (int i = 0; i < value.size(); i++) {
//                    msgCek.add(value.get(i).toString());
//                }
//
////                System.out.println(msgCek);
//                //List<String> hashSatu = input.MerkleTree.getNewMsgList(msgCek);
//               // List<String> hashDua = input.MerkleTree.hashRekursif(hashSatu);
//
//                String hashtoString = hashDua.toString(); //nilai hash yang dihashinh dari pesan yang ada digrup pesan
//
//                String hashMsgtoString = hashMsg.toString(); //nilai hash yang ada diproperti pesan
//
//                if (hashtoString != hashMsgtoString) {
//                    trustVal = trustVal - 0.1;
//
//                } else {
//                    trustVal = trustVal + 0.1;
//
//                }
//
//            }
////            //deteksi 1
//        } catch (Exception e) {
//        }
//    }
}
