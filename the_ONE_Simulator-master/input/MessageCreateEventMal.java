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
        String msgToString = null;
        Map<List<String>, List<Message>> mapHash = new HashMap<>();
        Map<List<String>, List<Message>> mapHash2 = new HashMap<>();

        for (int i = 0; i < jumPesan; i++) {
            String name = this.prefix + (this.nomor + i);

            Message m = new Message(from, to, name, drawMessageSize());
            m.setResponseSize(this.responseSize);

            messages.add(m);
            mtostring.add(m.toString());

        }

        String hashSatu;

        String hashDua;
//        List<String> hashTiga = MerkleTree.getNewMsgList_3(hashDua);
//        List<String> hashEmpat = MerkleTree.getNewMsgList_4(hashTiga);

        for (Message m : messages) {
            hashSatu = MerkleTree.getNewMsgList(m.toString());
            Tuple<String, String> tuple1 = new Tuple(m.toString(), hashSatu);
            System.out.println("tup 1 : " + tuple1);

            for (int i = 0; i < jumPesan; i++) {
                hashDua = MerkleTree.getNewMsgList(hashSatu);
                Tuple<String, String> tuple2 = new Tuple(m.toString(), hashDua);
                System.out.println("tup 2 " + tuple2);
            }

        }

        for (Message m : messages) {
            m.addProperty("hashSatu", mapHash);

            from.createNewMessage(m);
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
