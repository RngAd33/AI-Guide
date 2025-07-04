package com.rngad33.aiguide.model.entity;

import lombok.Data;

import java.util.List;

/**
 * 聊天室模型
 */
@Data
public class ChatRoom {

    /**
     * 房间号
     */
    private Long chatId;

    /**
     * 消息列表
     */
    private List<String> chatMessage;

}