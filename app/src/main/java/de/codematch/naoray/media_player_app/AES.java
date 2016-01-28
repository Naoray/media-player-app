package de.codematch.naoray.media_player_app;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    //Needs to be the same key in the php-file
    //Has to have a length of 16
    protected static final String key = "1234567890123456";

    /**
     * Encrypts the text with AES/CBC/PKCS5Padding
     *
     * @param text for encryption
     * @return encrypted String
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String text) throws GeneralSecurityException, UnsupportedEncodingException {

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
        return Base64.encodeToString(results, Base64.CRLF); //Base64.CRLF is very important here
    }

    /**
     * Decrypts the text with AES/CBC/PKCS5Padding
     *
     * @param text for decryption
     * @return decrypted String
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     * @Info: currently not used
     */
    public static String decrypt(String text)
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
    }
}