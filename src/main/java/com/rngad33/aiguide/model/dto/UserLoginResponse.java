package com.rngad33.aiguide.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录响应体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse extends User {

    private String token;

}