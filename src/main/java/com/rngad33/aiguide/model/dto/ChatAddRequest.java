package com.rngad33.aiguide.model.dto;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 对话创建请求体
 */
@Data
public class ChatAddRequest implements Serializable {

    /**
     * 房间号
     */
    private Long chatId;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息列表
     */
    private List<Message> chatMessage;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 3191241716373120793L;

}