/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 *
 * @author M S I
 */
public class EpidemicMerkleTree {

//    private final String pesan;
//    private final int id;
//
//    public EpidemicMerkleTree(String pesan, int id) {
//        this.pesan = pesan;
//        this.id = id;
//    }
    public String createMerkleTree(ArrayList<String> nodeLists) {
        ArrayList<String> merkleRoot = merkleTree(nodeLists);
        return merkleRoot.get(0);
    }

    private ArrayList<String> merkleTree(ArrayList<String> hashList) {
        //kembalian Merkle root
        if (hashList.size() == 1) {
            return hashList;
        }

        ArrayList<String> parentHashList = new ArrayList<>();
        //hash leaf dari pasangan node untuk mendapat hash lagi dari pasangan tsb
        for (int i = 0; i < hashList.size(); i += 2) {
            String hashedString = getSHA(hashList.get(i).concat(hashList.get(i + 1)));
            parentHashList.add(hashedString);
        }
        //jika jumlah nodenya ganjil, tambahkan lagi node terakhir
        if (hashList.size() % 2 == 1) {
            String lastHash = hashList.get(hashList.size() - 1);
            String hashedString = getSHA(lastHash.concat(lastHash));
            parentHashList.add(hashedString);
        }
        return merkleTree(parentHashList);
    }

    public static String getSHA(String input) {
        try {
            // method getInstance dipanggil dengan hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // method digest dipanggil
            // untuk menghitung message digest dari input dan mengembalikan array byte
            byte[] messageDigest = md.digest(input.getBytes());

            //mengubah array byte menjadi signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            //mengubah message digest ke nilai hexa
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } //kalau ada aloritma message digest yang salah
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown" + " for incorrect algorithm: " + e);
            return null;
        }
    }

    public static void main(String args[]) throws
            NoSuchAlgorithmException {

        ArrayList<String> nodeLists = new ArrayList<>();
        nodeLists.add("Lala");
        nodeLists.add("Lili");
        nodeLists.add("Lulu");
        nodeLists.add("Lilu");
        
        System.out.println("Hash 1 : " + getSHA("nodeLists"));

        String s1 = "Lala";
        System.out.println("\n" + s1 + " : " + getSHA(s1));

        String s2 = "Lili";
        System.out.println("\n" + s2 + " : " + getSHA(s2));
    }

}
