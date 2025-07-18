package com.rngad33.aiguide.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 心理咨询应用测试类
 */
@SpringBootTest
class PsychologyAppTest {

    @Resource
    private PsychologyApp psychologyApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // No.1
        String message = "你好，我是俊达萌，喜欢吃俊达饼";
        String answer = psychologyApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // No.2
        message = "你认识东北切蒲英吗？";
        answer = psychologyApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // No.3
        message = "我喜欢吃什么来着？";
        answer = psychologyApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是俊达萌，喜欢吃俊达饼";
        PsychologyApp.PsychologyReport answer = psychologyApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "最近学业繁忙，考试将即，如何缓解焦虑？";
        String answer = psychologyApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

}