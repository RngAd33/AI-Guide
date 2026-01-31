package com.rngad33.aiguide.constant;

/**
 * 密码学常量
 */
public interface CryptoConstant {

    // start of ECC
    String STD4ECC = "secp256r1";
    String HMAC = "HmacSHA256";
    // start of ECC

    // start of Argon2
    int ITERATIONS = 10;
    int MEMORY = 65536;
    int PARALLELISM = 1;
    // end of Argon2

    String CONFUSION = "***********";

    @Deprecated
    String TRANSFORMATION = "AES/CBC/PKCS5Padding";

}