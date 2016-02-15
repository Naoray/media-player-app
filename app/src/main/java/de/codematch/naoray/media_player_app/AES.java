package de.codematch.naoray.media_player_app;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * The Class AES contains Methods to en- & decrypt Strings with AESAlgorithm
 */
public class AES {

    //Needs to be the same key in the php-file
    //Has to have a length of 16
    protected static final String key = "1234567890123456";

    /**
     * Encrypt a String with AES-Algorithm
     * @param text Text, which has to be encypted
     * @return String
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String text) throws GeneralSecurityException, UnsupportedEncodingException {

        //Instance to encrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Check length of the password
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);

        //Generate key
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        //Generate initialization vector
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        //initialize cipher instance
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        //Encrypt
        byte[] results = cipher.doFinal(text.getBytes("ISO-8859-1"));
        return Base64.encodeToString(results, Base64.CRLF); //Base64.CRLF is very important here
    }

    /**
     * Decrypt a String with AES-Algorithm
     * @param text Text, which has to be decrypted
     * @return String
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String text)
            throws GeneralSecurityException, UnsupportedEncodingException {

        //Instance to decrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Check length of the password
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);

        //Generate key
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        //Generate initialization vector
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        //initialize cipher instance
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        //Decrypt
        byte[] results = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
        return new String(results, "UTF-8");
    }
}