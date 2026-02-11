package com.rngad33.aiguide.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * RAG向量模型
 */
@Data
@Table("app_vector")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppVector {

    /**
     * id
     */
    private Long id;

    /**
     * 向量名称
     */
    private String name;

    /**
     * 向量数据
     */
    private String vector;

    /**
     * 归属应用
     */
    private Integer appCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除？ 0-未删，1-已删
     */
    @Column(isLogicDelete = true)
    private Integer isDelete;

}