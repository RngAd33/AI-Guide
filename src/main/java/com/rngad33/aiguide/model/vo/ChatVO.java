package com.rngad33.aiguide.model.vo;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

import java.util.Date;
import java.util.List;

/**
 * 对话视图
 */
@Data
public class ChatVO {

    /**
     * 房间号
     */
    private Long chatId;

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

}