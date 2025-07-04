package com.rngad33.aiguide.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SpringAI调用AI
 * 取消注释即可在 SpringBoot 项目启动时执行
 */
// @Component
public class SpringAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = dashscopeChatModel.call(new Prompt("你好，我是普瑞塞斯"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}