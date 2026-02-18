package com.rngad33.aiguide.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.rngad33.aiguide.constant.RedisKeyConstant;
import com.rngad33.aiguide.mapper.ChatMapper;
import com.rngad33.aiguide.model.entity.Chat;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.model.enums.user.UserRoleEnum;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.ChatService;
import com.rngad33.aiguide.service.UserService;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * 对话记录服务实现
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 异步保存对话记录
     *
     * @param message
     * @param chatRoomId
     * @param answer
     * @param loginUser
     * @return
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CompletableFuture<Boolean> saveAsync(String message, String chatRoomId, String answer, UserVO loginUser) {
        ThrowUtils.throwIf(StrUtil.hasBlank(message, chatRoomId, answer), ErrorCodeEnum.PARAMS_ERROR, "无效的参数！");
        // 判断用户是否登录
        if (loginUser != null) {
            // 已登录，同步聊天记录到数据库
            Chat chat = new Chat();
            chat.setRoomId(Convert.bytesToLong(chatRoomId.getBytes()));
            chat.setQuestion(message);
            chat.setAnswer(answer);
            chat.setUserId(loginUser.getId());
            boolean result = this.save(chat);
            ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "保存对话记录失败！");
        } else {
            // 未登录，保存聊天记录到缓存
            redisTemplate.opsForValue().set(String.format(RedisKeyConstant.CHAT_FORMAT, chatRoomId), message + "|" + answer);
        }
        return CompletableFuture.completedFuture(true);
    }

    /**
     * 删除单个对话记录
     *
     * @param chatId
     * @param request
     * @return
     */
    @Override
    public boolean deleteChat(long chatId, HttpServletRequest request) {
        Chat chat;
        // 判断用户是否登录
        UserVO loginUser = userService.getCurrentUser(request);
        if (loginUser != null) {
            // - 已经登录，判断用户身份
            if (ObjUtil.equals(loginUser.getRole(), UserRoleEnum.ADMIN_ROLE.getCode())) {
                // - 管理员有权任意删除
                chat = this.getById(chatId);
            } else {
                // - 普通用户只能删除自己创建的对话记录
                QueryWrapper queryWrapper = QueryWrapper.create()
                        .eq("id", chatId)
                        .eq("user_id", loginUser.getId());
                chat = this.getOne(queryWrapper);
            }
            ThrowUtils.throwIf(ObjUtil.isNull(chat), ErrorCodeEnum.NOT_PARAMS, "找不到该对话记录！");
            boolean result = this.removeById(chat);
            ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "删除失败！");
        } else {
            // - 未登录，从Redis中删除
            redisTemplate.opsForValue().getAndDelete(String.format(RedisKeyConstant.CHAT_FORMAT, chatId));
        }
        return true;
    }

}