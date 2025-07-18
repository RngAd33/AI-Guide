package com.rngad33.aiguide.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 对话管理请求体
 */
@Data
public class ChatManageRequest implements Serializable {

    /**
     * 房间号
     */
    private Long chatId;

    private static final long serialVersionUID = 3191241716373120793L;

}