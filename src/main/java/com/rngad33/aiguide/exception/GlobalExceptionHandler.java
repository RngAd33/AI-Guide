package com.rngad33.aiguide.exception;

import com.rngad33.aiguide.common.BaseResponse;
import com.rngad33.aiguide.model.enums.misc.ErrorCodeEnum;
import com.rngad33.aiguide.utils.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public BaseResponse myExceptionHandler(MyException e) {
        log.error("————！！MyException: {}！！————", e.getMsg(), e);
        return ResultUtils.error(e.getCode(), e.getMsg());
    }

    /**
     * 捕获 Runtime 异常（重要！防止系统异常信息泄漏到前端）
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("————！！RuntimeException！！————", e);
        return ResultUtils.error(ErrorCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 处理媒体类型不支持异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException e) {
        log.error("Media type not acceptable", e);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Requested media type is not supported");
    }

}