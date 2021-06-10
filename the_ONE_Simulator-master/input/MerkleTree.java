/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author M S I
 */
public class MerkleTree {

    public static String getNewMsgList(String msgToString) {
     String hashSatu; 
     
     hashSatu = org.apache.commons.codec.digest.DigestUtils.sha1Hex(msgToString);
            
        
        return hashSatu;   
    }

    
    
    

    List<String> msgList;
    String root;

    public MerkleTree(List<String> msgList) {
        this.msgList = msgList;
        root = "";
    }

//    public static List<String> getNewMsgList(List<String> tempMsgList) {
//        List<String> hashSatu = new ArrayList<String>();
//
//        
//        for (String temp : tempMsgList) {
//            hashSatu.add(org.apache.commons.codec.digest.DigestUtils.sha1Hex(temp));
//        }
//        return hashSatu;
//    }

//    public static List<String> hashRekursif(List<String> masukan) {
//        List<String> a = masukan;
//        do {
//            a = getNewMsgList_2(a);
//        } while (a.size() != 1);
//        return a;
//    }

    public static List<String> getNewMsgList_2(List<String> tempMsgList_2) {
        List<String> hashDua = new ArrayList<String>();
        int index = 0;
        while (index < tempMsgList_2.size()) {
            String left = tempMsgList_2.get(index);
            index++;

            String right = "";
            if (index != tempMsgList_2.size()) {
                right = tempMsgList_2.get(index);
            }
            String sha1HexValue = org.apache.commons.codec.digest.DigestUtils.sha1Hex(left + right);
            hashDua.add(sha1HexValue);
            index++;
        }
        return hashDua;
    }

    public static List<String> getNewMsgList_3(List<String> tempMsgList_3) {
        List<String> hashTiga = new ArrayList<String>();
        int index = 0;
        while (index < tempMsgList_3.size()) {
            String left = tempMsgList_3.get(index);
            index++;

            String right = "";
            if (index != tempMsgList_3.size()) {
                right = tempMsgList_3.get(index);
            }
            String sha1HexValue = org.apache.commons.codec.digest.DigestUtils.sha1Hex(left + right);
            hashTiga.add(sha1HexValue);
            index++;
        }
        return hashTiga;
    }
    
     public static List<String> getNewMsgList_4(List<String> tempMsgList_4) {
        List<String> hashEmpat = new ArrayList<String>();
        int index = 0;
        while (index < tempMsgList_4.size()) {
            String left = tempMsgList_4.get(index);
            index++;

            String right = "";
            if (index != tempMsgList_4.size()) {
                right = tempMsgList_4.get(index);
            }
            String sha1HexValue = org.apache.commons.codec.digest.DigestUtils.sha1Hex(left + right);
            hashEmpat.add(sha1HexValue);
            index++;
        }
        return hashEmpat;
    }
     
     
}
