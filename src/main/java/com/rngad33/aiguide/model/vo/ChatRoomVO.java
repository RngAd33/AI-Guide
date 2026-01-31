package com.rngad33.aiguide.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.model.entity.ChatRoom;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 聊天室视图
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomVO {

    /**
     * 标题
     */
    private String title;

    /**
     * 对话内容（JSON）
     */
    private List<String> content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 对象转封装类
     *
     * @param chatRoom
     * @return
     */
    public static ChatRoomVO objToVo(ChatRoom chatRoom) {
        if (chatRoom == null) {
            return null;
        }
        ChatRoomVO chatRoomVO = new ChatRoomVO();
        BeanUtil.copyProperties(chatRoom, chatRoomVO, "content");
        chatRoomVO.setContent(JSONUtil.toList(chatRoom.getContent(), String.class));
        return chatRoomVO;
    }

    /**
     * 封装类转对象
     *
     * @param chatRoomVO
     * @return
     */
    public static ChatRoom voToObj(ChatRoomVO chatRoomVO) {
        if (chatRoomVO == null) {
            return null;
        }
        ChatRoom chatRoom = new ChatRoom();
        BeanUtil.copyProperties(chatRoomVO, chatRoom, "content");
        chatRoom.setContent(JSONUtil.toJsonStr(chatRoomVO.getContent()));
        return chatRoom;
    }

}