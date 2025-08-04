package com.rngad33.aiguide.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjUtil;
import com.rngad33.aiguide.app.LoveApp;
import com.rngad33.aiguide.app.PsychologyApp;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.dto.chat.ChatStartRequest;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI会话接口
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private PsychologyApp psychologyApp;

    /**
     * 恋爱大师
     *
     * @param request
     * @return
     */
    @PostMapping("/love")
    public BaseResponse<String> loveChat(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的对话请求！");
        String chatId = request.getChatId();
        String message = request.getMessage();
        String result = loveApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

    /**
     * 小姐姐心理疏导
     *
     * @param request
     * @return
     */
    @PostMapping("/psy")
    public BaseResponse<String> psyChat(@RequestBody ChatStartRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的对话请求！");
        String chatId = UUID.randomUUID().toString();
        String message = request.getMessage();
        String result = psychologyApp.doChat(message, chatId);
        return ResultUtils.success(result);
    }

}