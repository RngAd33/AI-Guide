package com.rngad33.aiguide.model.enums.app;

import lombok.Getter;

/**
 * 应用名称枚举
 */
@Getter
public enum AppNameEnum {

    LOVE(1, "love"),
    PSYCHOLOGY(2, "psychology"),
    TETO(3, "teto"),
    GAME(4, "game");

    private final Integer value;
    private final String name;

    AppNameEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 根据value获取枚举
     *
     * @param value
     * @return
     */
    public static AppNameEnum getByValue(Integer value) {
        for (AppNameEnum appNameEnum : AppNameEnum.values()) {
            if (appNameEnum.value.equals(value)) {
                return appNameEnum;
            }
        }
        return null;
    }
}