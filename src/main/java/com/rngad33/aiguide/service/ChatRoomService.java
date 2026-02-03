package com.rngad33.aiguide.service;

import com.mybatisflex.core.service.IService;
import com.rngad33.aiguide.model.dto.ChatRoomCreateRequest;
import com.rngad33.aiguide.model.dto.ChatRoomEditRequest;
import com.rngad33.aiguide.model.entity.ChatRoom;

/**
 * 聊天室接口
 */
public interface ChatRoomService extends IService<ChatRoom> {

    /**
     * 创建聊天室
     *
     * @param chatRoomCreateRequest
     * @return
     */
    long createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest);

    /**
     * 编辑聊天室信息
     *
     * @param chatRoomEditRequest
     * @return
     */
    boolean editChatRoom(ChatRoomEditRequest chatRoomEditRequest);

}