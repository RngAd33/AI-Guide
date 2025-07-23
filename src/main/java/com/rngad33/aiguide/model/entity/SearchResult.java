package com.rngad33.aiguide.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 搜索结果模型
 */
@Data
@Builder
public class SearchResult {

    /**
     * id
     */
    private String id;

    /**
     * 分数
     */
    private double score;

    /**
     * 标题
     */
    private String title;

    /**
     *  内容
     */
    private String content;

    /**
     * 网址
     */
    private String url;

    /**
     * 元数据
     */
    private String metadata;

    /**
     * 格式化方法
     *
     * @return
     */
    public String toString() {
        return String.format(
                "SearchResult{id='%s', score=%.2f, title='%s', content='%s', url='%s'}", id, score, title,
                content != null ? content.substring(0, Math.min(50, content.length())) + "..." : "null", url
        );
    }

}