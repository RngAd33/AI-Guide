package com.rngad33.aiguide.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.model.entity.Chat;
import lombok.Data;

import java.util.Date;

/**
 * 对话记录视图
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatVO {

    /**
     * 用户提问
     */
    private String question;

    /**
     * AI 回复
     */
    private String answer;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 对象转封装类
     *
     * @param chat
     * @return
     */
    public static ChatVO objToVo(Chat chat) {
        if (chat == null) {
            return null;
        }
        ChatVO chatVO = new ChatVO();
        BeanUtil.copyProperties(chat, chatVO);
        return chatVO;
    }

    /**
     * 封装类转对象
     *
     * @param chatVO
     * @return
     */
    public static Chat voToObj(ChatVO chatVO) {
        if (chatVO == null) {
            return null;
        }
        Chat chat = new Chat();
        BeanUtil.copyProperties(chatVO, chat);
        return chat;
    }

}