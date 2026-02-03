package com.rngad33.aiguide.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.rngad33.aiguide.annotation.NoWriteService;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.dto.ChatRoomCreateRequest;
import com.rngad33.aiguide.model.dto.ChatRoomEditRequest;
import com.rngad33.aiguide.model.entity.ChatRoom;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.model.enums.user.UserRoleEnum;
import com.rngad33.aiguide.model.vo.ChatRoomVO;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.ChatRoomService;
import com.rngad33.aiguide.service.UserService;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 聊天室接口
 */
@RestController
@RequestMapping("chat_room")
public class ChatRoomController {

    @Resource
    private ChatRoomService chatRoomService;

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建聊天室
     *
     * @param chatRoomCreateRequest
     * @param request
     * @return
     */
    @PostMapping("/create")
    public BaseResponse<Long> createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjUtil.hasNull(chatRoomCreateRequest, request), ErrorCodeEnum.NOT_PARAMS, "无效的请求！");
        // 判断当前用户是否登录
        UserVO loginUser = userService.getCurrentUser(request);
        if (loginUser != null) {
            // - 已登录，执行持久化创建
            return ResultUtils.success(chatRoomService.createChatRoom(chatRoomCreateRequest));
        } else {
            // - 未登录，执行临时创建
            redisTemplate.opsForValue().set(String.format("room_temp:%s", request.getHeader("User-Agent")), new ChatRoom(), 6000 + RandomUtil.randomInt(0, 60), TimeUnit.SECONDS);
            return ResultUtils.success(0L);
        }
    }

    /**
     * 查询当前用户创建的所有聊天室
     *
     * @param request
     * @return
     */
    @NoWriteService
    @GetMapping("/list/vo")
    public BaseResponse<List<ChatRoomVO>> listChatRoomVO(HttpServletRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.NOT_PARAMS, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        if (loginUser != null) {
            long userId = loginUser.getId();
            List<ChatRoom> list = chatRoomService.list(QueryWrapper.create().eq("user_id", userId));
            return ResultUtils.success(list.stream().map(ChatRoomVO::objToVo).toList());
        } else {
            // - 未登录
            redisTemplate.opsForValue().get(String.format("room_temp:%s", request.getHeader("User-Agent")));
            return ResultUtils.success(new ArrayList<>());
        }
    }

    /**
     * 编辑聊天室信息
     *
     * @param chatRoomEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChatRoom(@RequestBody ChatRoomEditRequest chatRoomEditRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjUtil.hasNull(chatRoomEditRequest, request), ErrorCodeEnum.NOT_PARAMS, "无效的请求！");
        String title = chatRoomEditRequest.getTitle();
        String chatRoomId = chatRoomEditRequest.getChatRoomId();
        ThrowUtils.throwIf(StrUtil.hasBlank(title, chatRoomId), ErrorCodeEnum.NOT_PARAMS, "缺少关键参数！");
        return ResultUtils.success(chatRoomService.editChatRoom(chatRoomEditRequest));
    }

    /**
     * 删除单个聊天室
     *
     * @param chatRoomId
     * @param request
     * @return
     */
    @NoWriteService
    @PostMapping("/del")
    public BaseResponse<Boolean> deleteChatRoom(@RequestParam("chatRoomId") String chatRoomId, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjUtil.isNull(request), ErrorCodeEnum.PARAMS_ERROR, "无效的请求！");
        UserVO loginUser = userService.getCurrentUser(request);
        long id = Convert.bytesToLong(chatRoomId.getBytes());
        if (ObjUtil.equals(loginUser.getRole(), UserRoleEnum.ADMIN_ROLE.getCode())) {
            ChatRoom chatRoom = chatRoomService.getById(id);
            ThrowUtils.throwIf(chatRoom == null, ErrorCodeEnum.NOT_PARAMS, "找不到该聊天室！");
            return ResultUtils.success(chatRoomService.removeById(chatRoom));
        } else {
            QueryWrapper queryWrapper = QueryWrapper.create().eq("id", id).eq("user_id", loginUser.getId());
            ChatRoom chatRoom = chatRoomService.getOne(queryWrapper);
            ThrowUtils.throwIf(chatRoom == null, ErrorCodeEnum.NOT_PARAMS, "找不到该聊天室！");
            return ResultUtils.success(chatRoomService.removeById(chatRoom));
        }
    }

}