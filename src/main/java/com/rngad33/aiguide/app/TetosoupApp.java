package com.rngad33.aiguide.app;

import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.manager.ChatManager;
import com.rngad33.aiguide.utils.AiModelUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * 海龟汤应用
 */
@Component
public class TetosoupApp {

    @Resource
    private ChatManager chatManager;

    private final ChatClient chatClient;

    /**
     * 初始化AI客户端
     *
     * @param chatModel
     */
    public TetosoupApp(AiModelUtils.MyChatModel chatModel) {
        // 初始化基于文件的对话记忆
        // ChatMemory chatMemoryByFile = new FileBaseChatMemory(FilePathConstant.FILE_MEMORY_PATH);
        // 初始化基于内存的对话记忆
        ChatMemory chatMemoryByCache = new InMemoryChatMemory();
        String SYSTEM_PROMPT = SystemPromptsConstant.TETO_SYSTEM_PROMPT;
        chatClient = ChatClient.builder(chatModel)
                .defaultSystem(SYSTEM_PROMPT)
                // 全局工具调用
                // .defaultTools(allTools)
                .defaultAdvisors(
                        // 文件对话记忆
                        // new MessageChatMemoryAdvisor(chatMemoryByFile)
                        // 内存对话记忆
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
     * 流式输出对话
     *
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatManager.doChatByStream(chatClient, message, chatId);
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