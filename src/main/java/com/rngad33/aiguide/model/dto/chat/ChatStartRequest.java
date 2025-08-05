package com.rngad33.aiguide.model.dto.chat;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用对话请求体
 */
@Data
public class ChatStartRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String chatId;

    private String message;

}