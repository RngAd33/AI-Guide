package com.rngad33.aiguide.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
    @TableId(type = IdType.AUTO)
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
    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 3191241716373120793L;

}