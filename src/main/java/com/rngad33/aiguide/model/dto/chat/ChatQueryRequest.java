package com.rngad33.aiguide.model.dto.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * 对话查询请求体
 */
@Data
public class ChatQueryRequest implements Serializable {

    /**
     * 房间号
     */
    private Long chatId;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建用户id
     */
    private Long userId;

    private static final long serialVersionUID = 3191241716373120793L;

}