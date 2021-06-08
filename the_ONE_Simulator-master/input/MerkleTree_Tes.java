/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author WINDOWS_X
 */
public class MerkleTree_Tes {
// A list of transaction

    List<String> txList;

    // Merkle Root
    String root;

    public MerkleTree_Tes(List<String> txList) {
        this.txList = txList;
        root = "";
    }

    public void merkle_tree(List<String> tempTxList) {
        tempTxList = new ArrayList<String>();

        for (int i = 0; i < this.txList.size(); i++) {
            tempTxList.add(this.txList.get(i));
        }

        List<String> newTxList = getNewTxList(tempTxList);
        while (newTxList.size() != 1) {
            newTxList = getNewTxList(newTxList);
        }

        this.root = newTxList.get(0);
    }

    public List<String> getNewTxList(List<String> tempTxList) {

        List<String> newTxList = new ArrayList<String>();
        int index = 0;
        while (index < tempTxList.size()) {
            // left
            String left = tempTxList.get(index);
            index++;

            // right
            String right = "";
            if (index != tempTxList.size()) {
                right = tempTxList.get(index);
            }

            // sha2 hex value
            String sha1HexValue = org.apache.commons.codec.digest.DigestUtils.sha1Hex(left + right);
            newTxList.add(sha1HexValue);
            index++;

        }

        return newTxList;
    }

    /**
     * Get Root
     *
     * @return
     */
    public String getRoot() {
        return this.root;
    }

}
