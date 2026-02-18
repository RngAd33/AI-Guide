package com.rngad33.aiguide.manager;

import com.rngad33.aiguide.app.App;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * AI对话管理器
 */
@Component
@Slf4j
public class AppChatManager {

    @Resource
    private ChatManager chatManager;

    @Resource
    private ChatService chatService;

    /**
     * 无框架流式对话
     *
     * @param message
     * @param chatId
     * @param fullResponse
     * @param sseEmitter
     * @param loginUser
     */
    public void doChatWithoutFrameworkByStream(String message, String chatId,  StringBuilder fullResponse, SseEmitter sseEmitter, UserVO loginUser) {
        chatManager.doChatWithoutFrameworkByStream(message)
                .subscribe(chunk -> {
                            try {
                                fullResponse.append(chunk);
                                sseEmitter.send(chunk);
                                // - 实时异步保存消息块
                            } catch (IOException e) {
                                sseEmitter.completeWithError(e);
                            }
                        }, sseEmitter::completeWithError,
                        () -> {
                            // - 保存记录后关闭SSE连接
                            sseEmitter.complete();
                            chatService.saveAsync(message, chatId, fullResponse.toString(), loginUser)
                                    .thenAccept(result -> {
                                        // 处理成功结果
                                        if (result) {
                                            // - 保存成功的后续操作
                                            log.info("对话记录 {} 已保存 >>>", chatId);
                                        }
                                    })
                                    .exceptionally(ex -> {
                                        // - 处理异常
                                        log.error("记录保存失败：" + ex.getMessage());
                                        return null;
                                    });
                        });
    }

    /**
     * 发起对话并异步保存记录
     *
     * @param app
     * @param message
     * @param chatId
     * @param fullResponse
     * @param sseEmitter
     * @param loginUser
     */
    public void doChatByStream(App app, String message, String chatId, StringBuilder fullResponse, SseEmitter sseEmitter, UserVO loginUser) {
        app.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                            try {
                                fullResponse.append(chunk);
                                sseEmitter.send(chunk);
                                // - 实时异步保存消息块
                            } catch (IOException e) {
                                sseEmitter.completeWithError(e);
                            }
                        }, sseEmitter::completeWithError,
                        () -> {
                            // - 保存记录后关闭SSE连接
                            sseEmitter.complete();
                            chatService.saveAsync(message, chatId, fullResponse.toString(), loginUser)
                                    .thenAccept(result -> {
                                        // 处理成功结果
                                        if (result) {
                                            // - 保存成功的后续操作
                                            log.info("对话记录 {} 已保存 >>>", chatId);
                                        }
                                    })
                                    .exceptionally(ex -> {
                                        // - 处理异常
                                        log.error("记录保存失败：" + ex.getMessage());
                                        return null;
                                    });
                        });
    }

    /**
     * RAG知识库流式对话流式对话（开启本地增强）
     *
     * @param app
     * @param message
     * @param chatId
     * @param fullResponse
     * @param sseEmitter
     * @param loginUser
     */
    public void doChatWithRagStream(App app, String message, String chatId, StringBuilder fullResponse, SseEmitter sseEmitter, UserVO loginUser) {
        app.doChatWithRagStream(message, chatId)
                .subscribe(chunk -> {
                            try {
                                fullResponse.append(chunk);
                                sseEmitter.send(chunk);
                                // - 实时异步保存消息块
                            } catch (IOException e) {
                                sseEmitter.completeWithError(e);
                            }
                        }, sseEmitter::completeWithError,
                        () -> {
                            // - 保存记录后关闭SSE连接
                            sseEmitter.complete();
                            chatService.saveAsync(message, chatId, fullResponse.toString(), loginUser)
                                    .thenAccept(result -> {
                                        // 处理成功结果
                                        if (result) {
                                            // - 保存成功的后续操作
                                            log.info("对话记录 {} 已保存 >>>", chatId);
                                        }
                                    })
                                    .exceptionally(ex -> {
                                        // - 处理异常
                                        log.error("记录保存失败：" + ex.getMessage());
                                        return null;
                                    });
                        });
    }

}