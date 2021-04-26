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
public class DistributedBubbleRap_connectionHist1 implements RoutingDecisionEngine, help {

    protected Map<DTNHost, Double> startTimeStamp;
    protected Map<DTNHost, List<Duration>> connHistory;

    double total = 0;
    double jumlah;

    public DistributedBubbleRap_connectionHist1(Settings s) {
        startTimeStamp = new HashMap<DTNHost, Double>();
        connHistory = new HashMap<DTNHost, List<Duration>>();
    }

    public DistributedBubbleRap_connectionHist1(DistributedBubbleRap_connectionHist1 proto) {
        startTimeStamp = new HashMap<DTNHost, Double>();
        connHistory = new HashMap<DTNHost, List<Duration>>();

    }

    @Override
    public void connectionUp(DTNHost thisHost, DTNHost peer) {

    }

    @Override
    public void connectionDown(DTNHost con, DTNHost peer) {
       //intracontact
        double time;
        if (startTimeStamp.get(peer) == null) { //waktu ketemu kita sama node lain belum pernah, waktunya diset 0
            time = 0;
        } else {
            time = startTimeStamp.get(peer); //kalo udah pernah ambil waktu kita ketemu
        }
        double etime = SimClock.getIntTime(); //ngambil waktu sekarang. waktu nya itu saat simulasi skrng

        List<Duration> history; //buat daftar kontaknya
        if (!connHistory.containsKey(peer)) {  //kalo belum pernah kontak sama node lain
            history = new LinkedList<Duration>(); //buat list kontak baru dengan node yg ditemui
            connHistory.put(peer, history); //masukin kita ketemu sama node itu dalam list history 
        } else {
            history = connHistory.get(peer); //kalo udah, kita ngambil history kita ketemu node itu
        }

        if (etime - time > 0) { //jika waktu pisah (simulasi skrng) - waktu ketemu lebih besar dari 0
            history.add(new Duration(time, etime)); //dihistory kita tambahin durasi baru yang isinya waktu ketemu dan waktu selesai ketemu
        }
        startTimeStamp.remove(peer); //buang waktu kita ketemu
        
//         //intercontact
////    DTNHost myHost = con.getOtherNode(peer); //
////        DistributedBubbleRap_connectionHist1 de = this.getOtherDecisionEngine(peer);
//        this.startTimeStamp.put(peer, SimClock.getTime()); //kita ketemu sama peer diambil waktunya terus dimasukin ke starttime
////        de.startTimeStamp.put(myHost, SimClock.getTime()); //peer ketemu sama kita diambil waktunya terus dimasukin ke starttime
    }
    
   

    @Override
    public void doExchangeForNewConnection(Connection con, DTNHost peer) { //fungsinya menyimpan nilai waktu mulai ketemu
        //intracontact
        DTNHost myHost = con.getOtherNode(peer); //
        DistributedBubbleRap_connectionHist1 de = this.getOtherDecisionEngine(peer);
        this.startTimeStamp.put(peer, SimClock.getTime()); //kita ketemu sama peer diambil waktunya terus dimasukin ke starttime
        de.startTimeStamp.put(myHost, SimClock.getTime()); //peer ketemu sama kita diambil waktunya terus dimasukin ke starttime

        //intercontact
//         double time;
//        if (startTimeStamp.get(peer) == null) { //waktu ketemu kita sama node lain belum pernah, waktunya diset 0
//            time = 0;
//        } else {
//            time = startTimeStamp.get(peer); //kalo udah pernah ambil waktu kita ketemu
//        }
//        double etime = SimClock.getIntTime(); //ngambil waktu sekarang. waktu nya itu saat simulasi skrng
//
//        List<Duration> history; //buat daftar kontaknya
//        if (!connHistory.containsKey(peer)) {  //kalo belum pernah kontak sama node lain
//            history = new LinkedList<Duration>(); //buat list kontak baru dengan node yg ditemui
//            connHistory.put(peer, history); //masukin kita ketemu sama node itu dalam list history 
//        } else {
//            history = connHistory.get(peer); //kalo udah, kita ngambil history kita ketemu node itu
//        }
//
//        if (etime - time > 0) { //jika waktu pisah (simulasi skrng) - waktu ketemu lebih besar dari 0
//            history.add(new Duration(time, etime)); //dihistory kita tambahin durasi baru yang isinya waktu ketemu dan waktu selesai ketemu
//        }
//        startTimeStamp.remove(peer); //buang waktu kita ketemu
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

    private double getConnHis(DTNHost host) {
        List<Duration> contact = new LinkedList();
        if (connHistory.containsKey(host)) {
            contact = connHistory.get(host);
        }
        for (Iterator<Duration> iterator = contact.iterator();
                iterator.hasNext();) {
            Duration next = iterator.next();
            total = total + (next.end - next.start);
        }
        jumlah = contact.size();
        return total / jumlah;
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
    public RoutingDecisionEngine replicate() {
        return new DistributedBubbleRap_connectionHist1(this);
    }

    private DistributedBubbleRap_connectionHist1 getOtherDecisionEngine(DTNHost h) {
        MessageRouter otherRouter = h.getRouter();
        assert otherRouter instanceof DecisionEngineRouter : "This router only works "
                + "with other routers of same type";

        return (DistributedBubbleRap_connectionHist1) ((DecisionEngineRouter) otherRouter).getDecisionEngine();
    }

    @Override
    //intracontact
    public boolean shouldSendMessageToHost(Message m, DTNHost otherHost, DTNHost thisHost) {
        if (m.getTo() == otherHost) {
            return true;
        }
        DTNHost oth = m.getTo();
        DistributedBubbleRap_connectionHist1 de = getOtherDecisionEngine(otherHost);

        double peer = de.getConnHis(oth);
        double me = this.getConnHis(oth);
        if (me < peer) {
            return true;
        } else {
            return false;
        }
    
    //intercontact
//        if (m.getTo() == otherHost) {
//            return true;
//        }
//        DTNHost oth = m.getTo();
//        DistributedBubbleRap_connectionHist1 de = getOtherDecisionEngine(otherHost);
//
//        double peer = this.getConnHis(oth);
//        double me = this.getConnHis(oth);
//        if (me > peer) {
//            return true;
//        } else {
//            return false;
//        }
    }

    @Override
    public void update(DTNHost thisHost) {

    }

    @Override
    public double Rerata() {
//        List<Duration> contact = new LinkedList();
        double Total = 0;
        double rerata = 0;
        double Jumlah = 0;
        for (Map.Entry<DTNHost,  List<Duration>> entry : connHistory.entrySet()) {
            DTNHost key = entry.getKey();
            Total = Total + getConnHis(key);
            
        }
        
        Jumlah = connHistory.size();
        rerata = Total / Jumlah;
        
        return rerata;
    }

}
