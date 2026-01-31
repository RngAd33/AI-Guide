package com.rngad33.aiguide.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.rngad33.aiguide.constant.UserConstant;
import com.rngad33.aiguide.mapper.UserMapper;
import com.rngad33.aiguide.model.dto.UserLoginRequest;
import com.rngad33.aiguide.model.dto.UserLoginResponse;
import com.rngad33.aiguide.model.entity.User;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.model.enums.user.UserStatusEnum;
import com.rngad33.aiguide.model.vo.UserVO;
import com.rngad33.aiguide.service.UserService;
import com.rngad33.aiguide.utils.JwtTokenUtils;
import com.rngad33.aiguide.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 数据校验
        ThrowUtils.throwIf(ObjUtil.hasNull(userLoginRequest), ErrorCodeEnum.NOT_PARAMS, "无效的请求！");
        String phone = userLoginRequest.getPhone();
        String code = userLoginRequest.getCode();
        String password = userLoginRequest.getPassword();
        ThrowUtils.throwIf(StrUtil.isBlank(phone) || !PhoneUtil.isMobile(phone) || StrUtil.isAllBlank(code, password),
                ErrorCodeEnum.PARAMS_ERROR, "关键参数有误！");
        // 构造查询
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("phone", phone)
                .eq("code", code, StrUtil.isNotBlank(code))
                .eq("password", password, StrUtil.isNotBlank(password));
        // 判断用户是否存在
        User user = this.getOne(queryWrapper);
        if (user == null) {
            // - 新用户，先注册再登录
            User newUser = new User();
            BeanUtil.copyProperties(userLoginRequest, newUser);
            boolean result = this.save(newUser);
            ThrowUtils.throwIf(!result, ErrorCodeEnum.SYSTEM_ERROR, "注册失败！");
            return this.saveLoginStatusAndGetToken(request, newUser);
        } else {
            // - 已经注册，直接登录
            UserStatusEnum status = UserStatusEnum.getEnumByValue(user.getStatus());
            ThrowUtils.throwIf(ObjUtil.equals(status, UserStatusEnum.BAN_STATUS), ErrorCodeEnum.USER_NOT_AUTH, "该账号已被封禁！");
            return this.saveLoginStatusAndGetToken(request, user);
        }
    }

    /**
     * 获取当前用户登录态
     *
     * @param request http请求
     * @return 登录态
     */
    @Override
    public UserVO getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User loginUser = (User) userObj;
        ThrowUtils.throwIf(loginUser == null, ErrorCodeEnum.USER_NOT_LOGIN);
        final long id = loginUser.getId();
        ThrowUtils.throwIf(id <= 0, ErrorCodeEnum.PARAMS_ERROR, "用户id异常！");
        User user = this.getById(id);
        return UserVO.objToVo(user);
    }

    /**
     * 保存用户登录态
     *
     * @param request
     * @param user
     * @return
     */
    private UserLoginResponse saveLoginStatusAndGetToken(HttpServletRequest request, User user) {
        final String token = JwtTokenUtils.getToken(user);
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE, user);
        final String sessionId = session.getId();
        redisTemplate.opsForValue().set("user_session:" + user.getId(), sessionId);
        redisTemplate.opsForValue().set("session_user:" + sessionId, String.valueOf(user.getId()));
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        BeanUtil.copyProperties(UserVO.objToVo(user), userLoginResponse);
        userLoginResponse.setToken(token);
        return userLoginResponse;
    }

}