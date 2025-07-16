package com.rngad33.aiguide.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;

/**
 * Ollama调用Ai
 * 取消注释即可在 SpringBoot 项目启动时执行
 */
@Deprecated
// @Component
public class OllamaAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = ollamaChatModel.call(new Prompt("你是普瑞塞斯"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}