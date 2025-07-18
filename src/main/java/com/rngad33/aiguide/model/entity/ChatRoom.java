package com.rngad33.aiguide.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聊天室模型
 */
@Data
public class ChatRoom implements Serializable {

    /**
     * 房间号
     */
    private Long chatId;

    /**
     * 消息列表
     */
    private List<String> chatMessage;

    private static final long serialVersionUID = 3191241716373120793L;

}