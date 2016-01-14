package de.codematch.naoray.media_player_app;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public static void main(String[] args) {
        try {

            String key = "1234567890123456"; // have to be of length 16
            String ciphertext = AES.encrypt(key, "nico@web.de");
            System.out.println("encrypted value:" + ciphertext);
            System.out.println("decrypted value:" + AES.decrypt(key, ciphertext));

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String key, String text)
            throws GeneralSecurityException, UnsupportedEncodingException {


        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("ISO-8859-1"));
        return Base64.encodeToString(results, Base64.CRLF);

        /*byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                new IvParameterSpec("1234567890123456".getBytes("ISO-8859-1")));
        String s = new String(cipher.doFinal(value.getBytes(Charset.forName("UTF-8"))),"ISO-8859-1");
        //System.out.println(s + "Test");
        return s;*/
    }

    public static String decrypt(String key, String text)
            throws GeneralSecurityException, UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
        return new String(results, "UTF-8");

 /*       byte[] raw = key.getBytes(Charset.forName("UTF-8"));
        if (raw.length != 16) {
            throw new IllegalArgumentException("Invalid key size.");
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,
                new IvParameterSpec("1234567890123456".getBytes("ISO-8859-1")));
        byte[] original = cipher.doFinal(encrypted);
        String s = new String(original, Charset.forName("UTF-8"));
        //System.out.println(s);
        return s;*/
    }
}