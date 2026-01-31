package com.rngad33.aiguide.controller;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.rngad33.aiguide.annotation.AuthCheck;
import com.rngad33.aiguide.annotation.NoWriteService;
import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.constant.UserConstant;
import com.rngad33.aiguide.model.dto.UserLoginRequest;
import com.rngad33.aiguide.model.dto.UserLoginResponse;
import com.rngad33.aiguide.model.entity.User;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.UserService;
import com.rngad33.aiguide.utils.ResultUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取手机验证码
     *
     * @param phone
     * @return
     */
    @GetMapping("/get/verify")
    public BaseResponse<String> getVerifyCode(@RequestParam("phone") String phone) {
        ThrowUtils.throwIf(StrUtil.isBlank(phone), ErrorCodeEnum.NOT_PARAMS);

        return ResultUtils.success("验证码发送成功！");
    }

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjUtil.hasNull(userLoginRequest, request), ErrorCodeEnum.PARAMS_ERROR);
        return ResultUtils.success(userService.login(userLoginRequest, request));
    }

    /**
     * 获取当前用户登录态
     *
     * @param request
     * @return
     */
    @GetMapping("/get/current")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getCurrentUser(request));
    }

    /**
     * 用户封禁
     *
     * @param id
     * @return
     */
    @NoWriteService
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/ban")
    public BaseResponse<Boolean> banUser(@RequestParam("id") long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCodeEnum.PARAMS_ERROR, "无效的id！");
        User user = userService.getById(id);
        if (user.getStatus() == 0) {
            user.setStatus(1);
        } else {
            user.setStatus(0);
        }
        userService.updateById(user);
        return ResultUtils.success(true);
    }

    /**
     * 用户删除
     *
     * @param id
     * @return
     */
    @NoWriteService
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/del")
    public BaseResponse<Boolean> deleteUser(@RequestParam("id") long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCodeEnum.PARAMS_ERROR, "无效的id！");

        return ResultUtils.success(true);
    }

}