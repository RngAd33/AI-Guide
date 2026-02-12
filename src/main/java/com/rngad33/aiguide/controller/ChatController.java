package com.rngad33.aiguide.controller;

import cn.hutool.core.util.ObjUtil;
import com.rngad33.aiguide.annotation.NoWriteService;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.common.DeleteRequest;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对话记录接口
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    /**
     * 删除单个对话记录
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @NoWriteService
    @PostMapping("/del")
    public BaseResponse<Boolean> deleteChat(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjUtil.hasNull(deleteRequest, request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        long chatId = deleteRequest.getId();
        return ResultUtils.success(chatService.deleteChat(chatId, request));
    }

}