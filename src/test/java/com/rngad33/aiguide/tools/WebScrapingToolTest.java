package com.rngad33.aiguide.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebScrapingToolTest {

    @Resource
    private WebScrapingTool webScrapingTool;

    @Test
    void doScrape() {
        String result = webScrapingTool.doScrape("https://www.bilibili.com");
        Assertions.assertNotNull(result);
    }

}