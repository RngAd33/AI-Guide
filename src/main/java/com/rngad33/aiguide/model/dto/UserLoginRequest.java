package com.rngad33.aiguide.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 用户登录请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginRequest {

    /**
     * 手机号与验证码（注册、登录通用）
     */
    private String phone, code;

    /**
     * 密码（仅登录使用）
     */
    private String password;

}