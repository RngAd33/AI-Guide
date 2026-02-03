package com.rngad33.aiguide.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 通用删除请求
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeleteRequest {

    private Long id;

}