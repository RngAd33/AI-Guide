package com.rngad33.aiguide.manager;

import com.rngad33.aiguide.advisor.MyLoggerAdvisor;
import com.rngad33.aiguide.app.LoveApp;
import com.rngad33.aiguide.common.CommonReport;
import com.rngad33.aiguide.constant.AbstractChatMemoryAdvisorConstant;
import com.rngad33.aiguide.constant.SystemPromptsConstant;
import com.rngad33.aiguide.rag.factory.RagCustomAdvisorFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.rngad33.aiguide.constant.AbstractChatMemoryAdvisorConstant.DEFAULT_CHAT_MEMORY_RESPONSE_SIZE;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 通用对话方法
 */
@Component
@Slf4j
public class ChatManager {

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    // record CommonReport(String title, List<String> suggestions) {}

    /**
     * 开启基础对话（支持多轮对话）
     *
     * @param chatClient
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(ChatClient chatClient, String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                // 接收用户输入
                .user(message)
                // 历史上下文
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24)
                )
                // 开启日志
                // .advisors(new MyLoggerAdvisor())
                // 开启推理增强
                // .advisors(new MyRe2Advisor())
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
     * @return 响应式对象
     */
    public Flux<String> doChatByStream(ChatClient chatClient, String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24)
                )
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                .stream()
                .content();
    }

    /**
     * 结构化输出对话（不适用于深度思考大模型）
     *
     * @param message
     * @param chatId
     * @return
     */
    public CommonReport doChatWithReport(ChatClient chatClient, String SYSTEM_PROMPT, String message, String chatId) {
        CommonReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要严格按照JSON格式生成测试结果，标题为{用户名}的测试报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(AbstractChatMemoryAdvisorConstant.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(AbstractChatMemoryAdvisorConstant.
                                CHAT_MEMORY_RETRIEVE_SIZE_KEY, DEFAULT_CHAT_MEMORY_RESPONSE_SIZE)   // 最大记忆条数
                )
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                .call()
                .entity(CommonReport.class);
        log.info("report: {}", loveReport);
        return loveReport;
    }

    /**
     * RAG知识库对话（不开启增强）
     *
     * @param chatClient AI客户端
     * @param appVectorStore 本地知识库
     * @param message 传入消息
     * @param chatId
     * @return
     */
    public String doChatWithRag(ChatClient chatClient, VectorStore appVectorStore,
                                String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
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
     * @param appVectorStore 本地知识库
     * @param message 传入消息
     * @param chatId
     * @return
     */
    public String doChatWithRag(ChatClient chatClient, VectorStore pgVectorStore,
                                Advisor ragCloudAdvisor, VectorStore appVectorStore,
                                String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24))
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                // RAG知识库问答
                .advisors(new QuestionAnswerAdvisor(appVectorStore))
                // RAG检索增强（基于云知识库）
                .advisors(ragCloudAdvisor)
                // RAG检索增强（基于PgVector向量存储）
                .advisors(new QuestionAnswerAdvisor(pgVectorStore))
                // 自定义检索增强（文档查询器 + 上下文增强器）
                // .advisors(RagCustomAdvisorFactory.createRagCustomAdvisor(appVectorStore, "学习"))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 工具调用
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(ChatClient chatClient, String message, String chatId) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                // 历史上下文
                .advisors(spec -> spec.param(AbstractChatMemoryAdvisorConstant.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(AbstractChatMemoryAdvisorConstant.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                // 工具调用
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 调用 MCP 服务
     *
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMcp(ChatClient chatClient, String message, String chatId) {
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(AbstractChatMemoryAdvisorConstant.CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(AbstractChatMemoryAdvisorConstant.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                // 调用MCP服务
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}