package com.rngad33.aiguide.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.rngad33.aiguide.manager.ChatManager;
import com.rngad33.aiguide.mapper.ChatRoomMapper;
import com.rngad33.aiguide.model.dto.ChatRoomCreateRequest;
import com.rngad33.aiguide.model.dto.ChatRoomEditRequest;
import com.rngad33.aiguide.model.entity.Chat;
import com.rngad33.aiguide.model.entity.ChatRoom;
import com.rngad33.aiguide.model.enums.app.AppNameEnum;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.service.ChatRoomService;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 对话记录服务实现
 */
@Service
@Slf4j
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoom> implements ChatRoomService {

    @Resource
    private ChatManager chatManager;

    @Resource
    private ChatService chatService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建聊天室
     *
     * @param chatRoomCreateRequest
     * @return
     */
    @Override
    public long createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {
        String chatRoomId = chatRoomCreateRequest.getChatRoomId();
        int appCode = chatRoomCreateRequest.getAppCode();
        long userId = chatRoomCreateRequest.getUserId();
        AppNameEnum appNameEnum = AppNameEnum.getByValue(appCode);
        ThrowUtils.throwIf(appNameEnum == null, ErrorCodeEnum.NOT_PARAMS, "找不到对应的AI应用！");

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(Convert.bytesToLong(chatRoomId.getBytes()));
        chatRoom.setAppCode(appNameEnum.getValue());
        // 判断是否为注册用户
        if (userId > 0) {
            // - 是注册用户，创建聊天室并持久化
            chatRoom.setUserId(userId);
            boolean result = this.save(chatRoom);
            ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "创建聊天室失败！");
        } else {
            // - 临时用户，创建临时聊天室写入缓存
            redisTemplate.opsForValue().set(String.format("chat_room_temp:%s", chatRoom.getId()), chatRoom);
        }
        return chatRoom.getId();
    }

    /**
     * 编辑聊天室信息
     *
     * @param chatRoomEditRequest
     * @return
     */
    @Override
    public boolean editChatRoom(ChatRoomEditRequest chatRoomEditRequest) {
        String title = chatRoomEditRequest.getTitle();
        String chatRoomId = chatRoomEditRequest.getChatRoomId();
        long id = Convert.bytesToLong(chatRoomId.getBytes());
        ChatRoom chatRoom = this.getById(id);
        ThrowUtils.throwIf(chatRoom == null, ErrorCodeEnum.NOT_PARAMS, "找不到聊天室！");
        if (StrUtil.isBlank(chatRoom.getTitle())) {
            // 房间内第一次对话，由 AI 自动为房间取名
            Chat chat = chatService.getOne(QueryWrapper.create().eq("room_id", chatRoomId));
            try {
                title = chatManager.doChatWithoutFramework(chat.getAnswer());
            } catch (NoApiKeyException | InputRequiredException e) {
                log.error("——！AI起标题失败！——");
                return false;
            }
        }
        chatRoom.setTitle(title);
        boolean result = this.updateById(chatRoom);
        ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "聊天室信息编辑失败！");
        return true;
    }

}