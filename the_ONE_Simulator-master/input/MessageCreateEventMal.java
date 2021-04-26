/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package input;

import core.DTNHost;
import core.Message;
import core.World;
import java.util.ArrayList;
import java.util.List;
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

        for (int i = 0; i < jumPesan; i++) {
            String name = this.prefix + (this.nomor + i);

            Message m = new Message(from, to, name, drawMessageSize());
            m.setResponseSize(this.responseSize);

            messages.add(m);
            mtostring.add(m.toString());

        }
        List<String> hashSatu = MerkleTree.getNewMsgList(mtostring);
        List<String> hashDua = MerkleTree.hashRekursif(hashSatu);

        for (Message m : messages) {
            m.addProperty("hash", hashDua);
            from.createNewMessage(m);
        }
        //            cetak hash yang dibuat
//        System.out.println("CREATED");
//        System.out.println("from : " + from + " " + messages);
//        System.out.println("");
//             System.out.println("from :" + from);
//             System.out.println("\nhash 1="+hashSatu);
//             System.out.println("\nhash 2="+hashDua);
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
