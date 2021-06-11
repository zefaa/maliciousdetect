/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package input;

import core.DTNHost;
import core.Message;
import core.Tuple;
import core.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * External event for creating a message.
 */
public class MessageCreateEventMal extends MessageEvent {

    private int jumPesan, nomor;
    private int responseSize, sizeMin, sizeMax;
    private String prefix;
    private int[] sizeRange;
    protected Random rng;

    /**
     * Creates a message creation event with a optional response request
     *
     * @param from The creator of the message
     * @param to Where the message is destined to
     * @param id ID of the message
     * @param size Size of the message
     * @param responseSize Size of the requested response message or 0 if no
     * response is requested
     * @param time Time, when the message is created
     */
    public MessageCreateEventMal(int from, int to, String id, int jumPesan,
            int responseSize, double time, int nomor, String prefix, int sizeMin, int sizeMax) {
        super(from, to, id, time);
        this.jumPesan = jumPesan;
        this.nomor = nomor;
        this.prefix = prefix;
        this.responseSize = responseSize;
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        this.rng = new Random(prefix.hashCode());

    }

    /**
     * Creates the message this event represents.
     */
    @Override
    public void processEvent(World world) {
        DTNHost to = world.getNodeByAddress(this.toAddr);
        DTNHost from = world.getNodeByAddress(this.fromAddr);
        List<Message> messages = new ArrayList<>();
        List<String> mtostring = new ArrayList<>();
        List<Message> tempMsg = new ArrayList<>();
        Map<List<String>, List<Message>> mapHash = new HashMap<>();
        Map<List<String>, List<Message>> mapHash2 = new HashMap<>();

        for (int i = 0; i < jumPesan; i++) {
            String name = this.prefix + (this.nomor + i);

            Message m = new Message(from, to, name, drawMessageSize());
            m.setResponseSize(this.responseSize);

            messages.add(m);
            mtostring.add(m.toString());

        }

        List<String> msgSize = new ArrayList<>();

        for (Message m : messages) {
            msgSize.add(m.toString());
            String hashSatu = MerkleTree.getNewMsgList(m.toString());
            Tuple<String, String> tuple1 = new Tuple(m.toString(), hashSatu);
            m.addProperty("hashSatu", tuple1);
            System.out.println("tup 1 : " + tuple1);
        }

        for (int i = 0; i < messages.size(); i += 2) {
            Tuple<String, String> tupleKiri = (Tuple<String, String>) messages.get(i).getProperty("hashSatu");
            Tuple<String, String> tupleKanan = (Tuple<String, String>) messages.get(i + 1).getProperty("hashSatu");
            String gabungHash = tupleKiri.getValue() + tupleKanan.getValue();
            String hashDua = MerkleTree.getNewMsgList(gabungHash);
            List<String> key = new ArrayList<>();
            key.add(tupleKiri.getKey());
            key.add(tupleKanan.getKey());

            Tuple<List<String>, String> tuple2 = new Tuple(key, hashDua);
            messages.get(i).addProperty("hashDua", tuple2);
            messages.get(i + 1).addProperty("hashDua", tuple2);
            System.out.println("tup 2 : " + tuple2);
        }

        for (int i = 0; i < messages.size(); i += 4) {
            Tuple<List<String>, String> tupleKiri = (Tuple<List<String>, String>) messages.get(i).getProperty("hashDua");
            Tuple<List<String>, String> tupleKanan = (Tuple<List<String>, String>) messages.get(i + 2).getProperty("hashDua");
            String gabungHash = tupleKiri.getValue() + tupleKanan.getValue();
            String hashTiga = MerkleTree.getNewMsgList(gabungHash);
            List<String> key = new ArrayList<>();
            for (String keyKiri : tupleKiri.getKey()) {
                key.add(keyKiri);
            }
            for (String keyKanan : tupleKanan.getKey()) {
                key.add(keyKanan);
            }

            Tuple<List<String>, String> tuple3 = new Tuple(key, hashTiga);
            messages.get(i).addProperty("hashTiga", tuple3);
            messages.get(i + 1).addProperty("hashTiga", tuple3);
            messages.get(i + 2).addProperty("hashTiga", tuple3);
            messages.get(i + 3).addProperty("hashTiga", tuple3);
            System.out.println("tup 3 : " + tuple3);
        }

        for (int i = 0; i < messages.size(); i += 8) {
            Tuple<List<String>, String> tupleKiri = (Tuple<List<String>, String>) messages.get(i).getProperty("hashTiga");
            Tuple<List<String>, String> tupleKanan = (Tuple<List<String>, String>) messages.get(i + 4).getProperty("hashTiga");
            String gabungHash = tupleKiri.getValue() + tupleKanan.getValue();
            String hashEmpat = MerkleTree.getNewMsgList(gabungHash);
            List<String> key = new ArrayList<>();
            for (String keyKiri : tupleKiri.getKey()) {
                key.add(keyKiri);
            }
            for (String keyKanan : tupleKanan.getKey()) {
                key.add(keyKanan);
            }

            Tuple<List<String>, String> tuple4 = new Tuple(key, hashEmpat);
            messages.get(i).addProperty("hashEmpat", tuple4);
            messages.get(i + 1).addProperty("hashEmpat", tuple4);
            messages.get(i + 2).addProperty("hashEmpat", tuple4);
            messages.get(i + 3).addProperty("hashEmpat", tuple4);
            messages.get(i + 4).addProperty("hashEmpat", tuple4);
            System.out.println("tup 4 : " + tuple4);
//            for (Message m : messages) {
//                m.addProperty("hashSatu", mapHash);
//
//                from.createNewMessage(m);
//            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " [" + fromAddr + "->" + toAddr + "] "
                + " CREATE";
    }

    protected int drawMessageSize() {

        int sizeDiff = sizeMin == sizeMax ? 0
                : rng.nextInt(sizeMax - sizeMin);
        return sizeMin + sizeDiff;
    }
}
