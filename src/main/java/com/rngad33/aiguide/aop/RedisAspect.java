package com.rngad33.aiguide.aop;

import com.rngad33.aiguide.utils.CryptoUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Redis AOP
 * 可临时解决没有TLS加密导致的数据泄露问题
 */
@Aspect
@Component
@ConditionalOnProperty(name = "redis.encrypt.enabled", havingValue = "true", matchIfMissing = true)
public class RedisAspect {

    /**
     * 拦截 ValueOperations 的 get/set 方法
     */
    @Pointcut("execution(* org.springframework.data.redis.core.ValueOperations.get(..)) " +
            "|| execution(* org.springframework.data.redis.core.ValueOperations.set(..))")
    public void valueOps() {}

    /**
     * 处理切面
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("valueOps()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String method = pjp.getSignature().getName();
        // 写操作：加密第 2 个参数（value）
        if (method.startsWith("set")) {
            Object[] args = pjp.getArgs();
            if (args.length >= 2 && args[1] instanceof String) {
                args[1] = CryptoUtils.doSM4Encrypt((String) args[1]);
            }
            return pjp.proceed(args);
        }
        // 读操作：解密返回值
        if (method.equals("get") || method.equals("getAndDelete")) {
            Object result = pjp.proceed();
            if (result instanceof String) {
                // 对返回的字符串进行SM4解密
                return CryptoUtils.doSM4Decrypt((String) result);
            }
            return result;
        }
        // 其余方法原样通过
        return pjp.proceed();
    }

}