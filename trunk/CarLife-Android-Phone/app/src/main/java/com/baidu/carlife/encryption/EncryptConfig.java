
package com.baidu.carlife.encryption;

public class EncryptConfig {

    // Debug mode Turn on the switch (debug mode is set to true, unencrypted mode is set to false)
    public static final boolean DEBUG_ENABLE = true;
    // Both ends of the phone and the vehicle are AES-encrypted at the beginning (set to true during debug use and set
    // to false during normal use)
    public static final boolean AES_ENCRYPT_AS_BEGINE = false;

    // RSA Generates a seed for a random key pair
    public static String RSA_GEN_SEED = "woshisuijizifuchuan";
    // RSA conversion settings
    public static final String TRANSFORMATION_SETTING = "RSA/ECB/PKCS1Padding";
}
