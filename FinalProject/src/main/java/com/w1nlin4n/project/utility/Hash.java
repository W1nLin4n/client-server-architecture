package com.w1nlin4n.project.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    public static String hash(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        byte[] hashBytes = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return hashBytesToString(hashBytes);
    }

    private static String hashBytesToString(byte[] hashBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                sb.append('0');
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }
}
