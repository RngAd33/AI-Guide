package com.rngad33.aiguide.controller;

import cn.hutool.core.util.ObjUtil;
import com.rngad33.aiguide.agent.MyManus;
import com.rngad33.aiguide.app.LoveApp;
import com.rngad33.aiguide.app.PsychologyApp;
import com.rngad33.aiguide.app.TetosoupApp;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.dto.chat.ChatStartRequest;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.utils.AiModelUtils;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * @param request
     * @return
     */
    @Deprecated
    @PostMapping("/love/sync")
    public BaseResponse<String> loveChatSync(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = loveApp.doChat(request.getMessage(), request.getChatId());
        return ResultUtils.success(result);
    }

    /**
     * 恋爱大师（SSE模式）
     *
     * @param request
     * @return
     */
    @PostMapping("/love/sse")
    public BaseResponse<SseEmitter> loveChatSSE(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        psychologyApp.doChatByStream(request.getMessage(), request.getChatId())
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
     * @param request
     * @return
     */
    @Deprecated
    @PostMapping("/psy/sync")
    public BaseResponse<String> psyChatSync(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = psychologyApp.doChat(request.getMessage(), request.getChatId());
        return ResultUtils.success(result);
    }

    /**
     * 小姐姐心理疏导（SSE模式）
     *
     * @param request
     * @return
     */
    @PostMapping("/psy/sse")
    public BaseResponse<Flux<ServerSentEvent<String>>> psyChatSSE(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        Flux<ServerSentEvent<String>> result = psychologyApp.doChatByStream(request.getMessage(), request.getChatId())
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
        return ResultUtils.success(result);
    }

    /**
     * AI海龟汤（同步模式）
     *
     * @param request
     * @return
     */
    @Deprecated
    @PostMapping("/teto/sync")
    public BaseResponse<String> tetoSoupChatSync(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        String result = tetosoupApp.doChat(request.getMessage(), request.getChatId());
        return ResultUtils.success(result);
    }

    /**
     * AI海龟汤（SSE模式）
     *
     * @param request
     * @return
     */
    @PostMapping("/teto/sse")
    public BaseResponse<SseEmitter> tetoSoupChatSSE(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        SseEmitter sseEmitter = new SseEmitter(300000L);   // 5分钟超时
        // 获取Flux响应式数据流
        tetosoupApp.doChatByStream(request.getMessage(), request.getChatId())
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
     * @param request
     * @return
     */
    @PostMapping("/manus/")
    public BaseResponse<SseEmitter> doChatWithManus(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        MyManus myManus = new MyManus(allTools, chatModel);
        SseEmitter sseEmitter = myManus.run(request.getMessage());
        return ResultUtils.success(sseEmitter);
    }

}