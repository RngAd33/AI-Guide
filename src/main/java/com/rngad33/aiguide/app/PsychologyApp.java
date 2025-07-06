package com.rngad33.aiguide.app;

import com.rngad33.aiguide.advisor.MyLoggerAdvisor;
import com.rngad33.aiguide.chatmemory.FileBaseChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 心理咨询应用
 */
@Component
@Slf4j
public class PsychologyApp {

    @Resource
    private VectorStore psychologyAppVectorStore;

    record PsychologyReport(String title, List<String> suggestions) {}

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "你是一位二次元心理咨询师，" +
            "当客户向你提问时，你需要对其进行答疑解惑，提供心理疏导；" +
            "此外，在回答客户问题时，尽量带上一些颜文字以展现亲和力。";

    /**
     * 初始化AI客户端
     *
     * @param ollamaChatModel
     */
    public PsychologyApp(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {
        // 初始化基于文件的对话记忆
        // String fileDir = System.getProperty("user.dir") + "/tmp/chatHistory";
        // ChatMemory chatMemory = new FileBaseChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(ollamaChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                        // 自定义日志拦截器（按需开启）
                        , new MyLoggerAdvisor()
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
     * 结构化输出对话（不适用于深度思考大模型）
     *
     * @param message
     * @param chatId
     * @return
     */
    public PsychologyReport doChatWithReport(String message, String chatId) {
        PsychologyReport psychologyReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要严格按照JSON格式生成测试结果，标题为{用户名}的心理报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24)
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
    public String doChatWithVectorStore(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 24))
                // 开启日志
                .advisors(new MyLoggerAdvisor())
                // RAG知识库问答
                .advisors(new QuestionAnswerAdvisor(psychologyAppVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}