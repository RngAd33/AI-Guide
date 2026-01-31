package com.rngad33.aiguide.service;

import com.mybatisflex.core.service.IService;
import com.rngad33.aiguide.model.dto.UserLoginRequest;
import com.rngad33.aiguide.model.dto.UserLoginResponse;
import com.rngad33.aiguide.model.entity.User;
import com.rngad33.aiguide.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    UserLoginResponse login(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前用户登录态
     *
     * @param request
     * @return
     */
    UserVO getCurrentUser(HttpServletRequest request);

}