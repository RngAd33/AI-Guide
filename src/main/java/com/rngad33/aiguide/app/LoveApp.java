package com.rngad33.aiguide.app;

import com.rngad33.aiguide.advisor.MyLoggerAdvisor;
import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.manager.ChatManager;
import com.rngad33.aiguide.rag.custom.MyQueryRewriter;
import com.rngad33.aiguide.utils.AiModelUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.rngad33.aiguide.constant.AbstractChatMemoryAdvisorConstant.*;

/**
 * 恋爱大师
 */
@Component
@Slf4j
public class LoveApp {

    @Resource
    private ChatManager chatManager;

    @Resource
    private MyQueryRewriter queryRewriter;

    @Resource
    @Qualifier("loveAppVectorStore")
    private VectorStore loveAppVectorStore;

    @Resource
    @Qualifier("lovePgVectorStore")
    private VectorStore lovePgVectorStore;

    @Resource
    @Qualifier("loveAppRagCloudAdvisor")
    private Advisor loveAppRagCloudAdvisor;

    private final ChatClient chatClient;

    record LoveReport(String title, List<String> suggestions) {}

    /**
     * 初始化AI客户端
     *
     * @param chatModel
     */
    public LoveApp(AiModelUtils.MyChatModel chatModel) {
        // 初始化基于内存的对话记忆
        ChatMemory chatMemoryByCache = new InMemoryChatMemory();
        chatClient = ChatClient.builder(chatModel)
                .defaultSystem(SystemPromptsConstant.LOVE_SYSTEM_PROMPT)
                // 全局工具调用
                // .defaultTools(allTools)
                .defaultAdvisors(
                        // new MessageChatMemoryAdvisor(chatMemoryByFile)
                        new MessageChatMemoryAdvisor(chatMemoryByCache)
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
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SystemPromptsConstant.PSYCHOLOGY_SYSTEM_PROMPT +
                        "每次对话后都要严格按照JSON格式生成测试结果，标题为{用户名}的心理报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)   // 最大记忆条数
                )
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                .call()
                .entity(LoveApp.LoveReport.class);
        log.info("report: {}", loveReport);
        return loveReport;
    }

    /**
     * RAG知识库对话
     *
     * @param message 查询语句
     * @param chatId
     */
    public String doChatWithRag(String message, String chatId) {
        // 查询重写
        String rewritedMessage = queryRewriter.doRewrite(message);
        // 采用重写后的查询
        return chatManager.doChatWithRag(chatClient, lovePgVectorStore,
                loveAppRagCloudAdvisor, loveAppVectorStore,
                rewritedMessage, chatId);
    }

    /**
     * 工具调用
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId) {
        return chatManager.doChatWithTools(chatClient, message, chatId);
    }

    /**
     * 调用MCP服务
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMcp(String message, String chatId) {
        return chatManager.doChatWithMcp(chatClient, message, chatId);
    }

}