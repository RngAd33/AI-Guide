package com.rngad33.aiguide.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;

/**
 * LangChain4j 调用 Ai
 */
@Deprecated
public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatModel qwenModel = QwenChatModel.builder()
                .apiKey(System.getenv("SPRING_AI_DASHSCOPE_API_KEY"))
                .modelName("qwen-max")
                .build();
        String answer = qwenModel.chat("你好，我是普瑞塞斯");
        System.out.println(answer);
    }
}