package com.rngad33.aiguide.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.model.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * 用户视图
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

    /**
     * id
     */
    private Long id;

    /**
     * 电话
     */
    private String phone;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 用户身份：0-普通用户，1-管理员
     */
    private Integer role;

    /**
     * 用户状态：0-正常，1-封禁
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 对象转封装类
     *
     * @param user
     * @return
     */
    public static UserVO objToVo(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 封装类转对象
     *
     * @param userVO
     * @return
     */
    public static User voToObj(UserVO userVO) {
        if (userVO == null) {
            return null;
        }
        User user = new User();
        BeanUtil.copyProperties(userVO, user);
        return user;
    }

}