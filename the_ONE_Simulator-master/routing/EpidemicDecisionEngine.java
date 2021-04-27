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
import report.NodeMaliciousHelper;

/**
 *
 * @author M S I
 */
public class EpidemicDecisionEngine implements RoutingDecisionEngineMalicious, NodeMaliciousHelper {

    private LinkedList drop = new LinkedList();
    private double trustValue = 0.5;
    private Map<DTNHost, List<Message>> saveMsg = new HashMap<>(); // catat pengirim pesan dan pesan yang yang berhasil di transfer per koneksi

    public EpidemicDecisionEngine(Settings s) {

    }

    public EpidemicDecisionEngine(EpidemicDecisionEngine proto) {
        this.trustValue = proto.trustValue;

    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {
        List<Message> pesan = new ArrayList<Message>();
        saveMsg.put(peer, pesan);
    }

    @Override
    public void connectionDown(DTNHost thisHost, DTNHost peer) {
        System.out.println("node : " + peer + " trust : " + trustValue);
        List<Message> psn = saveMsg.get(peer);

        //panggil method verifikasi
        verifikasiPesan(psn);

        saveMsg.remove(peer);

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

    @Override //buat sifat malicious
    public boolean shouldSaveReceivedMessage(Message m, DTNHost thisHost, DTNHost from) {
        List<Message> psn = saveMsg.get(from);
        try {
            psn.add(m);
        } catch (Exception e) {
        }

        /**
         * buat sifat malicious
         */
        if (thisHost.toString().startsWith("mal")) { //jika nama grup node diawali dengan 3 string "mal"
            //maka jalankan di bawah ini, pesan akan dibuang secara random sebanyak 50%

            Random rng = new Random();
            int rand = 1 * rng.nextInt() + 0;

            switch (rand) {
                case 0:
                    return m.getTo() != thisHost;
                default:
                    drop.add(m);
                    return false;

            }
            //jika nama grup node tidak diawal dengan 3 string mal maka  pesan akan disimpan dalam buffer node untuk diteruskan ke node berikutnya 
        } else {
            return m.getTo() != thisHost;
        }

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(DTNHost thisHost) {

    }

    @Override
    public RoutingDecisionEngineMalicious replicate() {
        return new EpidemicDecisionEngine(this);
    }

    @Override
    public LinkedList<Message> getNodeMalicious() {
        return drop;
    }

    //method verifikasi
    public void verifikasiPesan(List<Message> psn) {
        //gruping pesan
        Map<List<String>, List<Message>> grupPesan = new HashMap<>(); //simpan pesan ke dalam grup pake Map, key : properti hash; value : id pesan
        List<Message> pesan;
        try {

            for (Message m : psn) { //baca isi list psn satu persatu
                List<String> hashMsg = (List<String>) m.getProperty("hash"); //ambil property hash yang ada di tiap pesan

                if (!grupPesan.containsKey(hashMsg)) { //cek tidak ada grup pesan dengan hash tertentu
                    pesan = new ArrayList<Message>(); // buat arraylist untuk menampung pesan dalam grup dengan hash yang baru
                } else {
                    pesan = grupPesan.get(hashMsg);    //jika ada, get hash dari grup pesan

                }

                pesan.add(m); //tambahkan message dalam pesan
                grupPesan.put(hashMsg, pesan); //tambahkan hash dan id pesan dalam grup pesan
//                System.out.println("grup" + grupPesan);

                for (Message cekPsn : pesan) {
                    System.out.println(pesan);
                    List<String> msgCek = new ArrayList<>();
                    msgCek.add(cekPsn.toString());
                    List<String> hashSatu = input.MerkleTree.getNewMsgList(msgCek);
                    List<String> hashDua = input.MerkleTree.hashRekursif(hashSatu);
//                    System.out.println("hash " + hashDua);
                    String hashtoString = hashDua.toString(); //nilai hash yang dihashinh dari pesan yang ada digrup pesan
                    String hashMsgtoString = hashMsg.toString(); //nilai hash yang ada diproperti pesan

                    if (hashtoString != hashMsgtoString) {
                        trustValue = trustValue - 0.1;
                    } else {
//                        trustValue =  trustValue;
                    }
                }
            }

        } catch (Exception e) {
        }

        //deteksi 1
    }

}
