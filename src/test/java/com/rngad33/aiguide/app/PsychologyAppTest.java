package com.rngad33.aiguide.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
        message = "你认识春日部釉吗？她不理我了";
        answer = psychologyApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // No.3
        message = "我喜欢吃什么？";
        answer = psychologyApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是俊达萌，喜欢吃俊达饼";
        PsychologyApp.PsychologyReport answer = psychologyApp.doChatWithReport(message, chatId);
    }

}