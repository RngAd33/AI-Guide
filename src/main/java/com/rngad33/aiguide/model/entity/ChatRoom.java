package com.rngad33.aiguide.model.entity;

import com.mybatisflex.annotation.*;
import lombok.Data;
import org.springframework.ai.chat.messages.Message;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 聊天室模型
 *
 * @TableName chat_room
 */
@Data
@Table(value = "chat_room")
public class ChatRoom implements Serializable {

    /**
     * 房间 id
     */
    @Id(keyType = KeyType.Auto)
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

    /**
     * 是否删除？ 0-未删，1-已删
     */
    @Column(isLogicDelete = true)
    private Integer isDelete;

    @Column
    private static final long serialVersionUID = 3191241716373120793L;

}