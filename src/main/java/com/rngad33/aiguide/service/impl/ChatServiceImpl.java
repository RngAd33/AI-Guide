package com.rngad33.aiguide.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.rngad33.aiguide.mapper.ChatMapper;
import com.rngad33.aiguide.model.entity.Chat;
import com.rngad33.aiguide.model.entity.User;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.utils.ThrowUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对话记录服务实现
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    /**
     * 异步保存对话记录
     *
     * @param message
     * @param chatRoomId
     * @param answer
     * @param userId
     * @return
     */
    @Async
    @Override
    public boolean saveAsync(String message, String chatRoomId, String answer, long userId) {
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatRoomId, answer) || userId <= 0, ErrorCodeEnum.PARAMS_ERROR, "无效的参数！");
        Chat chat = new Chat();
        chat.setChatRoomId(Convert.bytesToLong(chatRoomId.getBytes()));
        chat.setUserId(userId);
        chat.setQuestion(message);
        chat.setAnswer(answer);
        boolean result = this.save(chat);
        ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "保存对话记录失败！");
        return true;
    }

}