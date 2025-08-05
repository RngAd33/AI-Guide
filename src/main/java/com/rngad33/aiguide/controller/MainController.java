package com.rngad33.aiguide.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局通用接口
 */
@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查
     *
     * @return
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

}