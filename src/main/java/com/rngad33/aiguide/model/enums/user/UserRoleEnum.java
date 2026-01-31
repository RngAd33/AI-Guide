package com.rngad33.aiguide.model.enums.user;

import lombok.Getter;

/**
 * 用户身份枚举
 */
@Getter
public enum UserRoleEnum {

    ADMIN_ROLE("admin", 1),
    DEFAULT_ROLE("user", 0);

    private final String role;
    private final Integer code;

    UserRoleEnum(String role, Integer code) {
        this.role = role;
        this.code = code;
    }

    /**
     * 根据 code 获取枚举
     *
     * @param code
     * @return
     */
    public static UserRoleEnum getEnumByCode(Integer code) {
        for (UserRoleEnum value : UserRoleEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}