package com.rngad33.aiguide.app;

import com.rngad33.aiguide.common.CommonReport;
import reactor.core.publisher.Flux;

/**
 * 应用接口
 */
public interface App {

    /**
     * 开启基础对话（支持多轮对话）
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return AI回复内容
     */
    String doChat(String message, String chatId);

    /**
     * 流式输出对话
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return 响应式流数据
     */
    Flux<String> doChatByStream(String message, String chatId);

    /**
     * 结构化输出对话（不适用于深度思考大模型）
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return 结构化报告
     */
    CommonReport doChatWithReport(String message, String chatId);

    /**
     * RAG知识库对话
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return AI回复内容
     */
    String doChatWithRag(String message, String chatId);

    /**
     * RAG知识库对话（流式输出）
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return AI回复内容
     */
    Flux<String> doChatWithRagStream(String message, String chatId);

    /**
     * 工具调用
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return AI回复内容
     */
    String doChatWithTools(String message, String chatId);

    /**
     * 调用MCP服务
     *
     * @param message 用户输入消息
     * @param chatId 聊天室ID
     * @return AI回复内容
     */
    String doChatWithMcp(String message, String chatId);

}