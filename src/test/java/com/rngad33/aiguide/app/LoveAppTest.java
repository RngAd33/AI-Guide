package com.rngad33.aiguide.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

/**
 * 恋爱大师测试类
 */
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        String message = "社恐怎么找对象？";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithStream() {
        String chatId = UUID.randomUUID().toString();
        String message = "社恐怎么找对象？";
        Flux<String> answer = loveApp.doChatWithStream(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "社恐怎么找对象？";
        LoveApp.LoveReport answer = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "社恐怎么找对象？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        String chatId = UUID.randomUUID().toString();
        String message = "社恐怎么找对象？";
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "社恐怎么找对象？";
        String answer = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }

}