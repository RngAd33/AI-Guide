package com.rngad33.aiguide.model.enums.app;

import com.rngad33.aiguide.constant.SystemPromptsConstant;
import lombok.Getter;

/**
 * APP信息枚举
 */
@Getter
public enum SystemPromptsEnum {

    PSYCHOLOGY("psychology", SystemPromptsConstant.PSYCHOLOGY);

    private final String name;

    private final String systemPrompt;

    SystemPromptsEnum(String name, String systemPrompt) {
        this.name = name;
        this.systemPrompt = systemPrompt;
    }

}