package com.rngad33.aiguide.controller;

import cn.hutool.core.util.StrUtil;
import com.rngad33.aiguide.agent.MyManus;
import com.rngad33.aiguide.app.LoveApp;
import com.rngad33.aiguide.app.PsychologyApp;
import com.rngad33.aiguide.app.TetosoupApp;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.utils.AiModelUtils;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * AI会话接口
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private AiModelUtils.MyChatModel chatModel;

    @Resource
    private LoveApp loveApp;

    @Resource
    private PsychologyApp psychologyApp;

    @Resource
    private TetosoupApp tetosoupApp;

    /**
     * 恋爱大师（同步模式）
     * 
     * @param chatId
     * @param message
     * @return
     */
    @Deprecated
    @GetMapping("/love/sync")
    public BaseResponse<String> loveChatSync(@RequestParam("chatId") String chatId,
                                             @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = loveApp.doChat(chatId, message);
        return ResultUtils.success(result);
    }

    /**
     * 恋爱大师（SSE模式）
     *
     * @param chatId
     * @param message
     * @return
     */
    @GetMapping("/love/sse")
    public BaseResponse<SseEmitter> loveChatSSE(@RequestParam("chatId") String chatId,
                                                @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        psychologyApp.doChatByStream(chatId, message)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return ResultUtils.success(sseEmitter);
    }

    /**
     * 小姐姐心理疏导（同步模式）
     *
     * @param chatId
     * @param message
     * @return
     */
    @Deprecated
    @GetMapping("/psy/sync")
    public BaseResponse<String> psyChatSync(@RequestParam("chatId") String chatId,
                                            @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = psychologyApp.doChat(chatId, message);
        return ResultUtils.success(result);
    }

    /**
     * 小姐姐心理疏导（SSE模式）
     *
     * @param chatId
     * @param message
     * @return
     */
    @GetMapping("/psy/sse")
    public BaseResponse<Flux<ServerSentEvent<String>>> psyChatSSE(@RequestParam("chatId") String chatId,
                                                                  @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        Flux<ServerSentEvent<String>> result = psychologyApp.doChatByStream(chatId, message)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
        return ResultUtils.success(result);
    }

    /**
     * AI海龟汤（同步模式）
     *
     * @param chatId
     * @param message
     * @return
     */
    @Deprecated
    @GetMapping("/teto/sync")
    public BaseResponse<String> tetoSoupChatSync(@RequestParam("chatId") String chatId,
                                                 @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = tetosoupApp.doChat(chatId, message);
        return ResultUtils.success(result);
    }

    /**
     * AI海龟汤（SSE模式）
     *
     * @param chatId
     * @param message
     * @return
     */
    @GetMapping("/teto/sse")
    public BaseResponse<SseEmitter> tetoSoupChatSSE(@RequestParam("chatId") String chatId,
                                                    @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        tetosoupApp.doChatByStream(chatId, message)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return ResultUtils.success(sseEmitter);
    }

    /**
     * AI智能体对话（SSE模式）
     *
     * @param chatId
     * @param message
     * @return
     */
    @GetMapping("/manus/")
    public BaseResponse<SseEmitter> doChatWithManus(@RequestParam("chatId") String chatId,
                                                    @RequestParam("message") String message) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(chatId, message), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        MyManus myManus = new MyManus(allTools, chatModel);
        SseEmitter sseEmitter = myManus.run(message);
        return ResultUtils.success(sseEmitter);
    }

}