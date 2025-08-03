package com.rngad33.aiguide.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * APP混合测试
 */
@SpringBootTest
public class AppMixTest {

    @Resource
    private LoveApp loveApp;

    @Resource
    private PsychologyApp psychologyApp;

    @Test
    void doTest() {
        String uuid = UUID.randomUUID().toString();
        // 恋爱大师
        String message = "你是谁？";
        String result = loveApp.doChat(message, uuid);
        Assertions.assertNotNull(result);
        message = "社恐怎么找对象？";
        result = psychologyApp.doChat(message, uuid);
        Assertions.assertNotNull(result);
        // 小姐姐心理疏导
        message = "你是谁？";
        result = loveApp.doChat(message, uuid);
        Assertions.assertNotNull(result);
        message = "最近学业繁忙，考试将即，如何缓解焦虑？";
        result = psychologyApp.doChat(message, uuid);
        Assertions.assertNotNull(result);
    }

}