package bih.nic.in.policesoft.security;


import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import bih.nic.in.policesoft.utility.CommonPref;


public class Encriptor {

   public String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(CommonPref.CIPHER_TRANSFORMATION);
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

//        BASE64Decoder decoder = new BASE64Decoder();
        byte [] results = cipher.doFinal(Base64.decodeBase64(text.getBytes("UTF-8")));
        return new String(results,"UTF-8");

    }

   public String Encrypt(String text, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance(CommonPref.CIPHER_TRANSFORMATION);
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(results));


    }
}

