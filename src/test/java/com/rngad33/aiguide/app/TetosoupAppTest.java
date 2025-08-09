package com.rngad33.aiguide.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 海龟汤测试类
 */
@SpringBootTest
class TetosoupAppTest {

    @Resource
    private TetosoupApp tetosoupApp;

    @Test
    void doChat() {
        // start
        String chatId = UUID.randomUUID().toString();
        String message = "开始";
        String answer = tetosoupApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // stop
        message = "我不想玩了";
        answer = tetosoupApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatByStream() {
        // start
        String chatId = UUID.randomUUID().toString();
        String message = "开始";
        String answer = tetosoupApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // stop
        message = "我不想玩了";
        answer = tetosoupApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
    }

    @Test
    void doChatWithMcp() {
    }

}