package com.rngad33.aiguide.common;

import lombok.Data;

import java.util.List;

/**
 * 结构化输出模型
 */
@Data
public class CommonReport {

    private String title;

    private List<String> suggestions;

    public CommonReport() {}

    public CommonReport(String title, List<String> suggestions) {
        this.title = title;
        this.suggestions = suggestions;
    }

}