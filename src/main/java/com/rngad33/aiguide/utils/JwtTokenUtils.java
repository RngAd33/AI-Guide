package com.rngad33.aiguide.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rngad33.aiguide.exception.MyException;
import com.rngad33.aiguide.model.entity.User;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.util.Date;

/**
 * JWT工具类（Nimbus实现）
 */
public class JwtTokenUtils {

    // 使用 HMAC 算法生成签名密钥
    @Value("${spring.security.jwt.key}")
    private static String SECRET_KEY;
    
    // Token 有效期（毫秒）
    @Value("${spring.security.jwt.timeout}")
    private static long EXPIRATION_TIME;
    
    /**
     * 生成 JWT Token
     *
     * @param userDetails 用户信息
     * @return JWT Token 字符串
     */
    public static String getToken(User userDetails) {
        // 设置过期时间
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        // 构建JWT
        try {
            // 1. 创建Header
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    .type(JOSEObjectType.JWT)
                    .build();
            // 2. 创建Payload
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(String.valueOf(userDetails.getId()))
                    .issuer("user-center")
                    .issueTime(new Date())
                    .expirationTime(expirationDate)
                    .claim("username", userDetails.getUserName())
                    .build();
            // 3. 创建JWS对象
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            // 4. 创建签名器
            JWSSigner signer = new MACSigner(SECRET_KEY.getBytes());
            // 5. 签名
            signedJWT.sign(signer);
            // 6. 序列化为 Base64 字符串
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("token生成失败：", e);
        }
    }
    
    /**
     * 验证 JWT Token
     *
     * @param token JWT Token 字符串
     * @return Claims 集合
     */
    public static JWTClaimsSet validateToken(String token) {
        try {
            // 1. 解析 Token
            SignedJWT signedJWT = SignedJWT.parse(token);
            // 2. 验证签名
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("JWT签名验证失败！");
            }
            // 3. 验证过期时间
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (new Date().after(claimsSet.getExpirationTime())) {
                throw new MyException(ErrorCodeEnum.TOO_MANY_REQUESTS, "token已过期！");
            }
            return claimsSet;
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("token验证失败：", e);
        }
    }

    /**
     * 从请求头中提取 JWT Token
     *
     * @param request
     * @return
     */
    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}