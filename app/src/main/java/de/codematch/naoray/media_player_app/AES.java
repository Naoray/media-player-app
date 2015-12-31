package de.codematch.naoray.media_player_app;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public static void main(String[] args) {
        try {

            String key = "1234567890123456"; // have to be of length 16
            byte[] ciphertext = encrypt(key, "nico@web.de");
            System.out.println("encrypted value:" + ciphertext);
            System.out.println("decrypted value:" + (decrypt(key, ciphertext)));

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String key, String value)
            throws GeneralSecurityException, UnsupportedEncodingException {

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                new IvParameterSpec("1234567890123456".getBytes("ISO-8859-1")));
        return cipher.doFinal(value.getBytes(Charset.forName("UTF-8")));
    }

    public static String decrypt(String key, byte[] encrypted)
            throws GeneralSecurityException, UnsupportedEncodingException {

        byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                new IvParameterSpec("1234567890123456".getBytes("ISO-8859-1")));
        byte[] original = cipher.doFinal(encrypted);

        return new String(original, Charset.forName("UTF-8"));
    }
}