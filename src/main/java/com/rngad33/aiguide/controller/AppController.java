package com.rngad33.aiguide.controller;

import com.rngad33.aiguide.agent.MyManus;
import com.rngad33.aiguide.app.GameApp;
import com.rngad33.aiguide.app.LoveApp;
import com.rngad33.aiguide.app.PsychologyApp;
import com.rngad33.aiguide.app.TetosoupApp;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.service.UserService;
import com.rngad33.aiguide.utils.AiModelUtils;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/app")
public class AppController {

    @Resource
    private ChatService chatService;

    @Resource
    private UserService userService;

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

    @Resource
    private GameApp gameApp;

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
    public SseEmitter loveChatSSE(@RequestParam("message") String message, @RequestParam("chatId") String chatId,
                                  HttpServletRequest request) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        long userId = loginUser.getId();
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
                                 @RequestParam("chatId") String chatId, HttpServletRequest request) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        long userId = loginUser.getId();
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        StringBuilder fullResponse = new StringBuilder();
        // 获取Flux响应式数据流
        psychologyApp.doChatByStream(message, chatId)
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
                            boolean result = chatService.saveAsync(message, chatId, fullResponse.toString(), userId);   // 异步保存
                            ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "对话记录保存失败！");
                        });
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
     * 游戏王（SSE模式）
     */
    @GetMapping(value = "/game/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithGame(@RequestParam("message") String message, @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        gameApp.doChatByStream(message, chatId)
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
    @GetMapping(value = "/manus/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithManus(@RequestParam("message") String message, @RequestParam("chatId") String chatId) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        MyManus myManus = new MyManus(allTools, chatModel);
        SseEmitter sseEmitter = myManus.run(message);
        return sseEmitter;
    }

}