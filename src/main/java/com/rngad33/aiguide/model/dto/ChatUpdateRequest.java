package com.rngad33.aiguide.model.dto;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;

import java.util.Date;
import java.util.List;

/**
 * 对话更新请求体（仅管理员）
 */
@Data
public class ChatUpdateRequest {

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

    /**
     * 更新时间
     */
    private Date updateTime;

}