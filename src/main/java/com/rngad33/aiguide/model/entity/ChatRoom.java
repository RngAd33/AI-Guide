package com.rngad33.aiguide.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;
import org.springframework.ai.chat.messages.Message;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 聊天室模型
 */
@Data
public class ChatRoom implements Serializable {

    /**
     * 房间号
     */
    @Id(keyType = KeyType.Auto)
    private Long chatId;

    /**
     * 消息列表
     */
    private List<Message> chatMessage;

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

    private static final long serialVersionUID = 3191241716373120793L;

}