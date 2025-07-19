package com.rngad33.aiguide.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 通用对话方法
 */
@Component
@Slf4j
public class ChatManager {

    /**
     * 开启基础对话（支持多轮对话）
     *
     * @param chatClient
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(ChatClient chatClient, String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24)
                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 流式输出对话（暂不可用）
     *
     * @param chatClient
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatWithStream(ChatClient chatClient, String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24)
                )
                .stream()
                .chatResponse()
                .map(chatResponse -> chatResponse.getResult().getOutput().getText());

    }

    /**
     * RAG知识库对话（不开启增强）
     *
     * @param chatClient
     * @param appVectorStore
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(ChatClient chatClient, VectorStore appVectorStore, String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24))
                // 开启日志
                // .advisors(new MyLoggerAdvisor())
                // RAG知识库问答
                .advisors(new QuestionAnswerAdvisor(appVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * RAG知识库对话（开启增强）
     *
     * @param chatClient AI客户端
     * @param pgVectorStore PostgreSQL向量数据库
     * @param ragCloudAdvisor RAG云知识库
     * @param appVectorStore 应用向量存储
     * @param message 传入消息
     * @param chatId
     * @return
     */
    public String doChatWithRag(ChatClient chatClient, VectorStore pgVectorStore,
                                Advisor ragCloudAdvisor, VectorStore appVectorStore,
                                String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24))
                // 开启日志
                // .advisors(new MyLoggerAdvisor())
                // RAG知识库问答
                .advisors(new QuestionAnswerAdvisor(appVectorStore))
                // RAG检索增强（基于云知识库）
                .advisors(ragCloudAdvisor)
                // RAG检索增强（基于PgVector向量存储）
                .advisors(new QuestionAnswerAdvisor(pgVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}