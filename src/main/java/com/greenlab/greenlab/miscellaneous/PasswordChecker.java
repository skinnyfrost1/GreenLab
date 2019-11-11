package com.greenlab.greenlab.miscellaneous;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordChecker {

    private static final String ILLEGAL_CHARS = "<>{}[]|\\/'\"~` ";

    public static boolean validPassword(String password) {
        synchronized (ILLEGAL_CHARS) {
            for (int i = 0; i < password.length(); i++) {
                if (ILLEGAL_CHARS.indexOf(password.charAt(i)) >= 0)
                    return false;
            }
            return true;
        }
    }

    public static String encryptSHA512(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            //String hashtext = no.toString(16);
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
//                hashtext = "0" + hashtext;
                hashtext.insert(0, '0');
            }

            // return the HashText
            return hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
