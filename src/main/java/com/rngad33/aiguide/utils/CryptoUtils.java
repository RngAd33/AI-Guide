package com.rngad33.aiguide.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.rngad33.aiguide.constant.CryptoConstant;
import com.rngad33.aiguide.exception.MyException;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import static cn.hutool.crypto.Mode.CBC;
import static cn.hutool.crypto.Padding.PKCS5Padding;

/**
 * 密码工具类
 *
 * @author RngAd33
 */
@Slf4j
public class CryptoUtils {

    // 本地持久密钥文件路径
    private static final String KEY_FILE_PATH = System.getProperty("user.dir") + File.separator +
            "src/main/java/com/rngad33/aiguide/model/temp/aes_key.bin";

    // 本地持久iv文件路径
    private static final String IV_FILE_PATH = System.getProperty("user.dir") + File.separator +
            "src/main/java/com/rngad33/aiguide/model/temp/aes_iv.bin";

    // Argon2 慢哈希加密器
    private static final Argon2 ARGON_2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    /**
     * 获取 ECC 密钥对
     *
     * @return eccKeyPair
     */
    public static KeyPair generateEccKeyPair() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
            kpg.initialize(new ECGenParameterSpec(CryptoConstant.STD4ECC));
            return kpg.generateKeyPair();
        } catch (Exception e) {
            log.error("ECC密钥对生成失败！", e);
            return null;
        }
    }

    /**
     * 获取 ECC 密钥对
     *
     * @return eccKeyMap
     */
    @Deprecated
    public static Map<String, String> getEccKeyMap() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "BC");
            kpg.initialize(new ECGenParameterSpec(CryptoConstant.STD4ECC));
            KeyPair eccKeyPair = kpg.generateKeyPair();
            String eccPublicKey = Base64.encode(eccKeyPair.getPublic().getEncoded());
            String eccPrivateKey = Base64.encode(eccKeyPair.getPrivate().getEncoded());
            return Map.of("publicKey", eccPublicKey, "privateKey", eccPrivateKey);
        } catch (Exception e) {
            log.error("ECC密钥对生成失败！", e);
            return null;
        }
    }

    /**
     * ECC加密（自定义公钥）
     *
     * @param text
     * @param eccPublicKey
     * @return
     */
    public static String doEccEncrypt(String text, String eccPublicKey) {
        try {
            KeyFactory KEY_REFACTOR = KeyFactory.getInstance("EC", "BC");
            byte[] publicKeyBytes = Base64.decode(eccPublicKey);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = KEY_REFACTOR.generatePublic(publicKeySpec);
            // - 创建ECC加密器
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // - 执行加密
            return Base64.encode(cipher.doFinal(text.getBytes()));
        } catch (Exception e) {
            log.error("ECC加密失败！", e);
            throw new MyException(ErrorCodeEnum.USER_LOSE_ACTION, "ECC加密失败！");
        }
    }

    /**
     * ECC解密（自定义私钥）
     *
     * @param cipherText
     * @param eccPrivateKey
     * @return licenseJson
     */
    public static String doEccDecrypt(String cipherText, String eccPrivateKey) {
        try {
            KeyFactory KEY_REFACTOR = KeyFactory.getInstance("EC", "BC");
            byte[] privateKeyBytes = Base64.decode(eccPrivateKey);
            PrivateKey privateKey = KEY_REFACTOR.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            // - 创建ECC解密器
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // - 执行解密
            return Convert.toStr(cipher.doFinal(Base64.decode(cipherText)));
        } catch (Exception e) {
            log.error("ECC解密失败！", e);
            throw new MyException(ErrorCodeEnum.USER_LOSE_ACTION, "ECC解密失败！");
        }
    }

    /**
     * 生成Jwt签名密钥（HMAC-SHA256）
     *
     * @param keyLength 密钥长度
     * @return 密钥 Base64
     */
    @Deprecated
    public static String getJwtSecureKey(int keyLength) {
        try {
            // 初始化密钥生成器
            KeyGenerator keyGen = KeyGenerator.getInstance(CryptoConstant.HMAC);
            keyGen.init(keyLength, new SecureRandom());
            // 生成、编码密钥
            return Base64.encode(keyGen.generateKey().getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("KeyGenerator初始化失败：", e);
        }
    }

    /**
     * 获取共享密钥（用于安全传递和挑战响应）
     *
     * @param otherPubBase 对方 ECC 公钥 Base64
     * @param privateKey 本地 ECC 私钥对象
     * @return sharedKey
     */
    public static String getSharedKey(String otherPubBase, PrivateKey privateKey) {
        try {
            // init
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");   // 此行代码调试耗时较长
            keyAgreement.init(privateKey);
            // 处理对方公钥
            KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
            byte[] otherPubBytes = Base64.decode(otherPubBase);
            // - 验证数据长度是否合理（防数据截断）X.509 EC公钥通常至少20字节以上
            ThrowUtils.throwIf(otherPubBytes.length < 20, ErrorCodeEnum.USER_LOSE_ACTION, "对方公钥数据长度异常");
            X509EncodedKeySpec otherPubSpec = new X509EncodedKeySpec(otherPubBytes);
            PublicKey otherPub = keyFactory.generatePublic(otherPubSpec);
            // 协商、计算共享密钥
            keyAgreement.doPhase(otherPub, true);
            return Base64.encode(keyAgreement.generateSecret());
        } catch (InvalidKeySpecException e) {
            log.error("公钥格式错误: {}", e.getMessage(), e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            log.error("算法不支持: {}", e.getMessage(), e);
            return null;
        } catch (NoSuchProviderException e) {
            log.error("安全提供者不可用: {}", e.getMessage(), e);
            return null;
        } catch (InvalidKeyException e) {
            log.error("密钥无效: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("ECC计算共享密钥未知错误: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 验证挑战响应码
     *
     * @param encryptedChallenge
     * @param checkChallenge
     * @param sharedKey
     * @param iv
     * @return
     */
    public static boolean verifyChallengeResponse(String encryptedChallenge, String checkChallenge, byte[] sharedKey, byte[] iv) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, sharedKey, iv);
        String challenge = sm4Model.decryptStr(encryptedChallenge);
        if (StrUtil.equals(challenge, checkChallenge)) {
            return true;
        }
        return false;
    }

    /**
     * SM4加密（使用本地持久密钥）
     *
     * @param text 待加密字段
     * @return 密文 Base64 编码
     */
    public static String doSM4Encrypt(String text) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, loadKey(), loadIV());
        return sm4Model.encryptBase64(text);
    }

    /**
     * SM4解密（使用本地持久密钥）
     *
     * @param cipherTest
     * @return
     */
    public static String doSM4Decrypt(String cipherTest) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, loadKey(), loadIV());
        return sm4Model.decryptStr(cipherTest);
    }

    /**
     * SM4加密（使用共享密钥）
     *
     * @param text 待加密字段
     * @return 密文 Base64 编码
     */
    public static String doSM4Encrypt(String text, String sharedKey, String iv) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, sharedKey.getBytes(), iv.getBytes());
        return sm4Model.encryptBase64(text);
    }

    /**
     * SM4解密（使用共享密钥）
     *
     * @param cipherTest
     * @param sharedKey
     * @param iv
     * @return
     */
    public static String doSM4Decrypt(String cipherTest, String sharedKey, String iv) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, sharedKey.getBytes(), iv.getBytes());
        return sm4Model.decryptStr(cipherTest);
    }

    /**
     * SM4加密（使用本地持久密钥）
     *
     * @param text 待加密字段
     * @return 密文 Argon2 摘要
     */
    public static String doEncryptArgon2Hash(String text) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, loadKey(), loadIV());
        return ARGON_2.hash(CryptoConstant.ITERATIONS, CryptoConstant.MEMORY, CryptoConstant.PARALLELISM, sm4Model.encryptBase64(text).getBytes());
    }

    /**
     * SM4加密（使用共享密钥）
     *
     * @param text 待加密字段
     * @param sharedKey 共享密钥
     * @param iv 偏移向量
     * @return 密文 Argon2 摘要
     */
    public static String doEncryptArgon2Hash(String text, String sharedKey, String iv) {
        SM4 sm4Model = new SM4(CBC, PKCS5Padding, sharedKey.getBytes(), iv.getBytes());
        return ARGON_2.hash(CryptoConstant.ITERATIONS, CryptoConstant.MEMORY, CryptoConstant.PARALLELISM, sm4Model.encryptBase64(text).getBytes());
    }

    /**
     * 验证 Argon2 慢哈希
     * 理论上使用此方法操作更安全
     *
     * @param argon2Hash
     * @param plainText
     * @return
     */
    public static boolean verifyArgon2Hash(String argon2Hash, char[] plainText) {
        return ARGON_2.verify(argon2Hash, plainText);
    }

    /**
     * 验证 Argon2 慢哈希
     *
     * @param argon2Hash
     * @param plainText
     * @return
     */
    public static boolean verifyArgon2Hash(String argon2Hash, String plainText) {
        return ARGON_2.verify(argon2Hash, plainText.getBytes());
    }

    /**
     * 生成初始化向量(IV)
     *
     * @return iv
     */
    public static byte[] generateIV() {
        byte[] iv = new byte[16];   // AES块大小是16字节
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * 从文件加载 SM4 密钥
     *
     * @return key
     */
    private static byte[] loadKey() {
        File keyFile = new File(KEY_FILE_PATH);
        if (!keyFile.exists()) {
            return null;
        }
        final byte[] key = new byte[(int) keyFile.length()];
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            fis.read(key);
        } catch (IOException e) {
            log.error("找不到文件", e);
            return null;
        }
        return key;
    }

    /**
     * 从文件加载 IV
     *
     * @return iv
     */
    private static byte[] loadIV() {
        File ivFile = new File(IV_FILE_PATH);
        if (!ivFile.exists()) {
            return null;
        }
        final byte[] iv = new byte[(int) ivFile.length()];
        try (FileInputStream fis = new FileInputStream(ivFile)) {
            fis.read(iv);
        } catch (IOException e) {
            log.error("找不到文件", e);
            return null;
        }
        return iv;
    }

}