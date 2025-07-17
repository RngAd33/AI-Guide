package com.rngad33.aiguide.app;

import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.manager.ChatManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 心理咨询应用
 */
@Component
@Slf4j
public class PsychologyApp {

    /**
     * 向量数据库Bean注入
     */
    @Resource
    private VectorStore psychologyAppVectorStore;

    @Resource
    private Advisor psychologyAppRagCloudAdvisor;

    @Resource
    private VectorStore pgVectorStore;

    @Resource
    private ChatManager chatManager;

    private final ChatClient chatClient;

    record PsychologyReport(String title, List<String> suggestions) {}

    /**
     * 初始化AI客户端
     *
     * @param ollamaChatModel
     */
    public PsychologyApp(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {
        // 初始化基于文件的对话记忆
        // String fileDir = System.getProperty("user.dir") + "/tmp/chatHistory1";
        // ChatMemory chatMemory = new FileBaseChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(ollamaChatModel)
                .defaultSystem(SystemPromptsConstant.PSYCHOLOGY)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                        // 自定义日志拦截器（按需开启）
                        // , new MyLoggerAdvisor()
                        // 自定义推理增强拦截器（按需开启）
                        // , new MyRe2Advisor()
                )
                .build();
    }

    /**
     * 开启基础对话（支持多轮对话）
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        return chatManager.doChat(chatClient, message, chatId);
    }

    /**
     * 流式输出对话（暂不可用）
     *
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatWithStream(String message, String chatId) {
        return chatManager.doChatWithStream(chatClient, message, chatId);
    }

    /**
     * 结构化输出对话（不适用于深度思考大模型）
     *
     * @param message
     * @param chatId
     * @return
     */
    public PsychologyReport doChatWithReport(String message, String chatId) {
        PsychologyReport psychologyReport = chatClient
                .prompt()
                .system(SystemPromptsConstant.PSYCHOLOGY + "每次对话后都要严格按照JSON格式生成测试结果，标题为{用户名}的心理报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24)   // 最大记忆条数
                )
                .call()
                .entity(PsychologyReport.class);
        log.info("report: {}", psychologyReport);
        return psychologyReport;
    }

    /**
     * RAG知识库对话
     *
     * @param message
     * @param chatId
     */
    public String doChatWithRag(String message, String chatId) {
        return chatManager.doChatWithRag(chatClient, psychologyAppRagCloudAdvisor, psychologyAppVectorStore, pgVectorStore, message, chatId);
    }

}