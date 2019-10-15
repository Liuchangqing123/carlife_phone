
package com.didi365.carlife.encryption;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class RSAManager {
    PublicKey mPublicKey;
    PrivateKey mPrivateKey;
    private String mPrivateKeyString = null;
    private String mPublicKeyString = null;

    public RSAManager() {
        keyPairGenerate();
    }

    public String getPublicKeyString() {
        return mPublicKeyString;
    }

    public PublicKey getmPubkey(String key) {
        byte[] keyBytes = Base64.decode(key, Base64.NO_WRAP);
        PublicKey publicKey = null;

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publicKey;
    }

    public String getPrivateKeyString() {
        return mPrivateKeyString;
    }

    public PrivateKey getPrivateKey() {
        return mPrivateKey;
    }

    public PublicKey getPublicKey() {
        return mPublicKey;
    }

    /**
     * Use private key to decrypt
     *
     * @param data
     * @param privateKey
     *
     * @return
     */
    public String decrypt(String data, PrivateKey privateKey) {
        byte[] rst = null;
        String rstStr = null;
        try {
            byte[] encodedContent = Base64.decode(data, Base64.NO_WRAP);
            Cipher cipher = Cipher.getInstance(EncryptConfig.TRANSFORMATION_SETTING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            rst = cipher.doFinal(encodedContent);
            rstStr = new String(rst, "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rstStr;
    }

    /**
     *  encrypted by public key
     *
     * @param data
     * @param publicKey
     *
     * @return
     */
    public String encrypt(String data, PublicKey publicKey) {
        byte[] rst = null;
        String rstStr = null;
        try {
            Cipher cipher = Cipher.getInstance(EncryptConfig.TRANSFORMATION_SETTING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipher.update(data.getBytes("UTF-8"));

            rst = cipher.doFinal();
            rstStr = Base64.encodeToString(rst, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rstStr;
    }

    public void publicKeyGenerate(String key) {
        try {
            mPublicKey = getPublicKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void keyPairGenerate() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");

            SecureRandom secrand = new SecureRandom();
            keygen.initialize(2048, secrand);
            KeyPair keys = keygen.genKeyPair();

            mPublicKey = keys.getPublic();
            mPrivateKey = keys.getPrivate();

            mPublicKeyString = Base64.encodeToString(mPublicKey.getEncoded(), Base64.NO_WRAP);
            mPrivateKeyString = Base64.encodeToString(mPrivateKey.getEncoded(), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
}
