package com.rngad33.aiguide.model.enums.user;

import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatusEnum {

    NORMAL_STATUS("正常", 0),
    BAN_STATUS("封禁", 1);

    private final String status;

    private final Integer value;

    UserStatusEnum(String status, Integer value) {
        this.status = status;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserStatusEnum getEnumByValue(Integer value) {
        for (UserStatusEnum valueEnum : UserStatusEnum.values()) {
            if (valueEnum.getValue().equals(value)) {
                return valueEnum;
            }
        }
        return null;
    }

}