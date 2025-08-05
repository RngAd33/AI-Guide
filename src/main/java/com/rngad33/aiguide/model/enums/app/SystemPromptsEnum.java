package com.rngad33.aiguide.model.enums.app;

import com.rngad33.aiguide.constant.SystemPromptsConstant;
import lombok.Getter;

/**
 * APP信息枚举
 */
@Getter
public enum SystemPromptsEnum {

    LOVE("love", SystemPromptsConstant.LOVE_SYSTEM_PROMPT),
    PSYCHOLOGY("psychology", SystemPromptsConstant.PSYCHOLOGY_SYSTEM_PROMPT),
    TETO("", SystemPromptsConstant.TETO_SYSTEM_PROMPT);

    private final String name;

    private final String systemPrompt;

    SystemPromptsEnum(String name, String systemPrompt) {
        this.name = name;
        this.systemPrompt = systemPrompt;
    }

}