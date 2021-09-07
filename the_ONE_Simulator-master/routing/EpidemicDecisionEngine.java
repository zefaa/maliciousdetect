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
import java.util.Random;
import static routing.EpidemicDecisionRouter.DEFAULT_CONTACT_INTERVAL;
import static routing.EpidemicDecisionRouter.TOTAL_CONTACT_INTERVAL;

/**
 *
 * @author M S I
 */
public class EpidemicDecisionEngine implements RoutingDecisionEngineMalicious, NodeMaliciousHelper {

    protected LinkedList<Double> resourcesList;
    public static final String TOTAL_CONTACT_INTERVAL = "perTotalContact";
    public static final int DEFAULT_CONTACT_INTERVAL = 300;
    private Double lastRecord = Double.MIN_VALUE;
    private int interval;
    private LinkedList drop = new LinkedList();
    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>(); // catat pengirim pesan dan pesan yang yang berhasil di transfer per koneksi
    private Map<DTNHost, List<Message>> saveMsgThis = new HashMap<>();
    List<Message> pesan = null;
    List<String> hashMsg = null;
    ArrayList<Integer> valList;
    int trysize = 0;
    double trustVal;
    List<DTNHost> maliciousList = new ArrayList<DTNHost>();

    public EpidemicDecisionEngine(Settings s) {
//        if (s.contains(TOTAL_CONTACT_INTERVAL)) {
//            interval = s.getInt(TOTAL_CONTACT_INTERVAL);
//        } else {
//            interval = DEFAULT_CONTACT_INTERVAL;
//        }

    }

    public EpidemicDecisionEngine(EpidemicDecisionEngine proto) {
//        resourcesList = new LinkedList<>();
//        interval = proto.interval;
//        lastRecord = proto.lastRecord;
//        this.trustValue = proto.trustValue;

    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {

       // trustVal = thisHost.getTrustValue(peer);
        List<Message> pesan = new ArrayList<Message>();
        saveMsg.put(peer, pesan);
        saveMsgThis.put(thisHost, pesan);

        try {

            for (Message msg : pesan) {
                if (trustVal >= 0.1) {
                    shouldSendMessageToHost(msg, peer, thisHost);
                }

            }
        } catch (Exception e) {
        }

    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {

        List<Message> psn = saveMsg.get(peer);
//        try {
//            for (Message m : psn) {
//                if (thisHost.toString().startsWith("mal")) { //jika nama grup node diawali dengan 3 string "mal"
//                    //maka jalankan di bawah ini, pesan akan dibuang secara random sebanyak 50%
//
//                    Random rng = new Random();
//                    int rand = 1 * rng.nextInt() + 0;
//
//                    switch (rand) {
//                        case 0:
//                            shouldSaveReceivedMessage(m, thisHost, peer);
//                        default:
//                            thisHost.deleteMessage(m.toString(), true);
//                            
//                    }
//                    //jika nama grup node tidak diawal dengan 3 string mal maka  pesan akan disimpan dalam buffer node untuk diteruskan ke node berikutnya 
//                } else {
//                    shouldSaveReceivedMessage(m, thisHost, peer);
//                }
//            }
//
//        } catch (Exception e) {
//        }

        //panggil method verifikasi
//        verifikasiPesan(psn);

        saveMsg.remove(peer);

        //deteksi 1
        try {
            
            for (Message msg : psn) {
                if (trustVal < 0.1) {
                    maliciousList.add(peer);                   
                }
                //System.out.println("mal " + maliciousList);
            }
        } catch (Exception e) {
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

    @Override //buat sifat malicious
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost,
            DTNHost from
    ) {
        List<Message> psn = saveMsg.get(from);
        Map<List<String>, List<Message>> sendMsgGrup = new HashMap<>();

//         try {
//
//            if (thisHost.toString().startsWith("mal")) { //jika nama grup node diawali dengan 3 string "mal"
//                //maka jalankan di bawah ini, pesan akan dibuang secara random sebanyak 50%
//
//                if (thisHost != m.getTo() && thisHost != m.getFrom()) {
//                    Random rng = new Random();
//                    int rand = 1 * rng.nextInt() + 0;
//
//                    switch (rand) {
//                        case 0:
//                            return m.getTo() != thisHost;
//                        default:
//                            drop.add(m);
//                            return false;
//
//                    }
//                }
//
//                //jika nama grup node tidak diawal dengan 3 string mal maka  pesan akan disimpan dalam buffer node untuk diteruskan ke node berikutnya 
//            } else {
//                return m.getTo() != thisHost;
//            }
//        } catch (Exception e) {
//        }
        
        
        try {
            psn.add(m);

            for (Message message : psn) {
                hashMsg = (List<String>) message.getProperty("hash");
                if (!sendMsgGrup.containsKey(hashMsg)) {
                    pesan = new ArrayList<Message>();

                } else {
                    pesan = sendMsgGrup.get(hashMsg);
                }
                pesan.add(message);
                sendMsgGrup.put(hashMsg, pesan);
            }
            List<Message> pesanTemp = new ArrayList<>();
            pesanTemp = sendMsgGrup.get(hashMsg);

            Map<List<Message>, Integer> conMsg = new HashMap<>();

            conMsg.put(pesanTemp, pesanTemp.size());
            trysize = pesanTemp.size();

            if (trysize == 8) {
                return m.getTo() != thisHost;
            }
        } catch (Exception e) {
        }

       
       
        return m.getTo() != thisHost;
    }

    @Override
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost,
            DTNHost thisHost
    ) {
//        int mSize = m.getSize();
//        //int nrMsg = otherHost.getNrofMessages();
//        int bOth = otherHost.getRouter().getFreeBufferSize();
//
//        int buf = bOth / mSize;
//        List<Message> shSend = saveMsgThis.get(thisHost);
//        List<Message> psn = saveMsg.get(otherHost);
//
//        try {
//            ArrayList<Integer> clMethod = new ArrayList<Integer>(this.shouldSendMessageBuffer(shSend));
//            int clMethodSize = clMethod.size();
//            for (DTNHost mal : maliciousList) {
//                if (otherHost.equals(mal)) {
//                    return false;
//                } else {
//                    if (clMethodSize < buf) {
//                        return true;
//                    }  
//                }
//            }
//            
//
//        } catch (Exception e) {
//        }
        return true;

    }

    @Override
    public boolean shouldDeleteSentMessage(Message m, DTNHost otherHost
    ) {
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
        return new EpidemicDecisionEngine(this);
    }

    @Override
    public LinkedList<Message> getNodeMalicious() {
        return drop;
    }
    //method kirim pesan bdk buffer

    public int shouldSendMessageBuffer(List<Message> sendMsg) {
        //gruping pesan
        Map<List<String>, List<Message>> sendMsgGrup = new HashMap<>();
        List<Message> pesanTemp = new ArrayList<>();
        try {
            for (Message message : sendMsg) {
                hashMsg = (List<String>) message.getProperty("hash");
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
    public void verifikasiPesan(List<Message> psn) {
        //gruping pesan
        Map<List<String>, List<Message>> grupPesan = new HashMap<>(); //simpan pesan ke dalam grup pake Map, key : properti hash; value : id pesan

        try {

            for (Message m : psn) { //baca isi list psn satu persatu
                hashMsg = (List<String>) m.getProperty("hashEmpat"); //ambil property hash yang ada di tiap pesan

                if (!grupPesan.containsKey(hashMsg)) { //cek tidak ada grup pesan dengan hash tertentu
                    pesan = new ArrayList<Message>(); // buat arraylist untuk menampung pesan dalam grup dengan hash yang baru
                } else {
                    pesan = grupPesan.get(hashMsg);    //jika ada, get hash dari grup pesan
                }

                pesan.add(m); //tambahkan message dalam pesan

                grupPesan.put(hashMsg, pesan); //tambahkan hash dan id pesan dalam grup pesan

            }
            for (Map.Entry<List<String>, List<Message>> entry : grupPesan.entrySet()) {
                List<String> key = entry.getKey();
                List<Message> value = entry.getValue();

//                System.out.println("val : " + value);
                List<Message> temp = value;
//                for (int i = 0; i < temp.size() - 1; i++) {
//                    for (int j = i + 1; j < temp.size() - j; j++) {

                for (int i = 0; i < temp.size() - 1; i++) {
                    for (int j = i + 1; j < temp.size(); j++) {
                        String str1 = temp.get(i).toString();
                        String str2 = temp.get(j).toString();
                        int m1 = str1.toCharArray().length;
                        int s1 = Integer.parseInt(str1.substring(1, m1));
                        int m2 = str2.toCharArray().length;
                        int s2 = Integer.parseInt(str2.substring(1, m2));
//                        int st = Integer.parseInt(str.toCharArray());
//                        System.out.println(j);
//                        System.out.println(temp.get(i) + " icreat : " + s1);
//                        System.out.println(temp.get(j) + " jcreate : " + s2);

                        if (s1 > s2) {
                            Message m = temp.get(i);
                            temp.set(i, temp.get(j));
                            temp.set(j, m);
                        }

                    }
                }
//                System.out.println("temp " + temp);
                grupPesan.replace(key, temp);
            }

//                System.out.println("val2 : " + value);
            for (Map.Entry<List<String>, List<Message>> entry : grupPesan.entrySet()) {
                List<String> key = entry.getKey();
                List<Message> value = entry.getValue();
                List<String> msgCek = new ArrayList<>();

                for (int i = 0; i < value.size(); i++) {
                    msgCek.add(value.get(i).toString());
                }

//                System.out.println(msgCek);
//                List<String> hashSatu = input.MerkleTree.getNewMsgList(msgCek);
//                List<String> hashDua = input.MerkleTree.hashRekursif(hashSatu);

//                String hashtoString = hashDua.toString(); //nilai hash yang dihashinh dari pesan yang ada digrup pesan
//
//                String hashMsgtoString = hashMsg.toString(); //nilai hash yang ada diproperti pesan

//                if (hashtoString != hashMsgtoString) {
//                    trustVal = trustVal - 0.1;
//                } else {
//                    trustVal = trustVal + 0.1;
//
//                }

            }
//            //deteksi 1
        } catch (Exception e) {
        }
    }
}
