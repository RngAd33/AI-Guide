package com.rngad33.aiguide.controller;

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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
     * @param message
     * @param chatId
     * @return
     */
    @Deprecated
    @GetMapping(value = "/love/sync", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<String> loveChatSync(@RequestParam("message") String message,
                                             @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = loveApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * 恋爱大师（SSE模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter loveChatSSE(@RequestParam("message") String message,
                                  @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        loveApp.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * 小姐姐心理疏导（同步模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @Deprecated
    @GetMapping(value = "/psy/sync", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<String> psyChatSync(@RequestParam("message") String message,
                                            @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = psychologyApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * 小姐姐心理疏导（SSE模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/psy/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter psyChatSSE(@RequestParam("message") String message,
                                 @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        psychologyApp.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * AI海龟汤（同步模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @Deprecated
    @GetMapping(value = "/teto/sync", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<String> tetoSoupChatSync(@RequestParam("message") String message,
                                                 @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = tetosoupApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * AI海龟汤（SSE模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/teto/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter tetoSoupChatSSE(@RequestParam("message") String message,
                                      @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        tetosoupApp.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * AI智能体对话（SSE模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/manus", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithManus(@RequestParam("message") String message,
                                      @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        MyManus myManus = new MyManus(allTools, chatModel);
        SseEmitter sseEmitter = myManus.run(message);
        return sseEmitter;
    }

}