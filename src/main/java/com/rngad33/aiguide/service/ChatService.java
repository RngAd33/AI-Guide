package com.rngad33.aiguide.service;

import com.mybatisflex.core.service.IService;
import com.rngad33.aiguide.model.entity.Chat;

/**
 * 对话记录服务接口
 */
public interface ChatService extends IService<Chat> {

    /**
     * 异步保存对话记录
     *
     * @param message
     * @param chatRoomId
     * @param answer
     * @param userId
     * @return
     */
    boolean saveAsync(String message, String chatRoomId, String answer, long userId);

}