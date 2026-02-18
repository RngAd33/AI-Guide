package com.rngad33.aiguide.controller;

import cn.hutool.core.util.StrUtil;
import com.rngad33.aiguide.agent.MyManus;
import com.rngad33.aiguide.app.*;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.manager.AppChatManager;
import com.rngad33.aiguide.manager.ChatManager;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.service.UserService;
import com.rngad33.aiguide.utils.AiModelUtils;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AppController {

    @Resource
    private AppChatManager appChatManager;

    @Resource
    private UserService userService;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private AiModelUtils.MyChatModel chatModel;

    @Resource
    private App loveApp;

    @Resource
    private App psychologyApp;

    @Resource
    private App tetosoupApp;

    @Resource
    private App gameApp;

    /**
     * 随想畅聊（SSE模式）
     *
     * @param message
     * @param chatId
     * @param request
     * @return
     */
    @GetMapping(value = "/nest/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter nestChatSSE(@RequestParam("message") String message,
                                  @RequestParam("chatId") String chatId, HttpServletRequest request) {
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        SseEmitter sseEmitter = new SseEmitter(300000L);
        StringBuilder fullResponse = new StringBuilder();
        appChatManager.doChatWithoutFrameworkByStream(message, chatId, fullResponse, sseEmitter, loginUser);
        return sseEmitter;
    }

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
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
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
                                  @RequestParam("chatId") String chatId, HttpServletRequest request) {
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        StringBuilder fullResponse = new StringBuilder();
        // 获取Flux响应式数据流
        appChatManager.doChatWithRagStream(loveApp, message, chatId, fullResponse, sseEmitter, loginUser);
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
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
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
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        StringBuilder fullResponse = new StringBuilder();
        // 获取Flux响应式数据流
        appChatManager.doChatWithRagStream(psychologyApp, message, chatId, fullResponse, sseEmitter, loginUser);
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
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
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
                                      @RequestParam("chatId") String chatId, HttpServletRequest request) {
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        StringBuilder fullResponse = new StringBuilder();
        // 获取Flux响应式数据流
        appChatManager.doChatByStream(tetosoupApp, message, chatId, fullResponse, sseEmitter, loginUser);
        return sseEmitter;
    }

    /**
     * 游戏王（SSE模式）
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/game/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter doChatWithGame(@RequestParam("message") String message,
                                     @RequestParam("chatId") String chatId, HttpServletRequest request) {
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        StringBuilder fullResponse = new StringBuilder();
        appChatManager.doChatWithRagStream(gameApp, message, chatId, fullResponse, sseEmitter, loginUser);
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
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatId), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        MyManus myManus = new MyManus(allTools, chatModel);
        SseEmitter sseEmitter = myManus.run(message);
        return sseEmitter;
    }

}