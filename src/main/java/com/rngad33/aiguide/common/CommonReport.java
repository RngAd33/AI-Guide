package com.rngad33.aiguide.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 结构化输出模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonReport {

    private String title;

    private List<String> suggestions;

}