package com.rngad33.aiguide.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.rngad33.aiguide.app.PsychologyApp;
import com.rngad33.aiguide.common.BaseResponse;
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
    private PsychologyApp psychologyApp;

    /**
     * 小姐姐心理疏导
     *
     * @param prompt
     * @return
     */
    @PostMapping("/psy")
    public BaseResponse<String> psyChat(@RequestBody String prompt) {
        ThrowUtils.throwIf(StrUtil.isBlank(prompt), ErrorCodeEnum.PARAMS_ERROR);
        String chatId = UUID.randomUUID().toString();
        String result =  psychologyApp.doChat(prompt, chatId);
        return ResultUtils.success(result);
    }

}