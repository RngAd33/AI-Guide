package com.rngad33.aiguide.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    /**
     * 状态码
     */
    private int code;

    /**
     * 泛型数据
     */
    private T data;

    /**
     * 消息
     */
    private String msg;

//    /**
//     * 有消息的返回
//     *
//     * @param code
//     * @param data
//     * @param msg
//     */
//    public BaseResponse(int code, T data, String msg) {
//        this.code = code;
//        this.data = data;
//        this.msg = msg;
//    }

    /**
     * 无消息的返回
     *
     * @param code
     * @param data
     */
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    /**
     * 返回错误信息
     *
     * @param errorCode
     */
    public BaseResponse(ErrorCodeEnum errorCode) {
        this(errorCode.getCode(), null, errorCode.getMsg());
    }

}