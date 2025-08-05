package com.rngad33.aiguide.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.rngad33.aiguide.agent.MyManus;
import com.rngad33.aiguide.app.LoveApp;
import com.rngad33.aiguide.app.PsychologyApp;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.utils.AiModelUtils;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 恋爱大师（同步模式）
     *
     * @param message
     * @return
     */
    @PostMapping("/love/sync")
    public BaseResponse<String> loveChatSync(@RequestBody String message) {
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCodeEnum.PARAMS_ERROR, "消息不能为空！");
        String chatId = UUID.randomUUID().toString();
        String result = loveApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * 恋爱大师（SSE模式）
     *
     * @param message
     * @return
     */
    @PostMapping("/love/sse")
    public BaseResponse<SseEmitter> loveChatSSE(@RequestBody String message) {
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCodeEnum.PARAMS_ERROR, "消息不能为空！");
        String chatId = UUID.randomUUID().toString();
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
        return ResultUtils.success(sseEmitter);
    }

    /**
     * 小姐姐心理疏导（同步模式）
     *
     * @param message
     * @return
     */
    @PostMapping("/psy/sync")
    public BaseResponse<String> psyChatSync(@RequestBody String message) {
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCodeEnum.PARAMS_ERROR, "消息不能为空！");
        String chatId = UUID.randomUUID().toString();
        String result = psychologyApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * 小姐姐心理疏导（SSE模式）
     *
     * @param message
     * @return
     */
    @PostMapping("/psy/sse")
    public BaseResponse<Flux<ServerSentEvent<String>>> psyChatSSE(@RequestBody String message) {
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCodeEnum.PARAMS_ERROR, "消息不能为空！");
        String chatId = UUID.randomUUID().toString();
        Flux<ServerSentEvent<String>> result = psychologyApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
        return ResultUtils.success(result);
    }

    /**
     * AI智能体对话（SSE模式）
     *
     * @param message
     * @return
     */
    @PostMapping("/manus")
    public BaseResponse<SseEmitter> doChatWithManus(@RequestBody String message) {
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCodeEnum.PARAMS_ERROR, "消息不能为空！");
        MyManus myManus = new MyManus(allTools, chatModel);
        SseEmitter sseEmitter = myManus.run(message);
        return ResultUtils.success(sseEmitter);
    }

}