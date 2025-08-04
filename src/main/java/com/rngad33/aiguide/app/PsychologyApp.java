package com.rngad33.aiguide.app;

import com.rngad33.aiguide.advisor.MyLoggerAdvisor;
import com.rngad33.aiguide.chatmemory.FileBaseChatMemory;
import com.rngad33.aiguide.common.CommonReport;
import com.rngad33.aiguide.constant.FilePathConstant;
import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.manager.ChatManager;
import com.rngad33.aiguide.rag.custom.MyQueryRewriter;
import com.rngad33.aiguide.utils.AiModelUtils.MyChatModel;
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
 * 小姐姐心理疏导
 */
@Component
@Slf4j
public class PsychologyApp {

    @Resource
    private ChatManager chatManager;

    @Resource
    private MyQueryRewriter queryRewriter;

    @Resource
    @Qualifier("psychologyAppVectorStore")
    private VectorStore psychologyAppVectorStore;

    @Resource
    @Qualifier("psychologyPgVectorStore")
    private VectorStore psychologyPgVectorStore;

    @Resource
    @Qualifier("psychologyAppRagCloudAdvisor")
    private Advisor psychologyAppRagCloudAdvisor;

    private final ChatClient chatClient;

    // record PsychologyReport(String title, List<String> suggestions) {}

    /**
     * 初始化AI客户端
     *
     * @param chatModel
     */
    public PsychologyApp(MyChatModel chatModel) {
        // 初始化基于文件的对话记忆
        ChatMemory chatMemoryByFile = new FileBaseChatMemory(FilePathConstant.FILE_MEMORY_PATH);
        // 初始化基于内存的对话记忆
        ChatMemory chatMemoryByCache = new InMemoryChatMemory();
        chatClient = ChatClient.builder(chatModel)
                .defaultSystem(SystemPromptsConstant.PSYCHOLOGY_SYSTEM_PROMPT)
                // 全局工具调用
                // .defaultTools(allTools)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemoryByFile)
                        // new MessageChatMemoryAdvisor(chatMemoryByCache)
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
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatManager.doChatByStream(chatClient, message, chatId);
    }

    /**
     * 结构化输出对话（不适用于深度思考大模型）
     *
     * @param message
     * @param chatId
     * @return
     */
    public CommonReport doChatWithReport(String message, String chatId) {
        return chatManager.doChatWithReport(chatClient, message, chatId);
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
        return chatManager.doChatWithRag(chatClient, psychologyPgVectorStore,
                psychologyAppRagCloudAdvisor, psychologyAppVectorStore,
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