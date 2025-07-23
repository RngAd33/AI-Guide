package com.rngad33.aiguide.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebScrapingToolTest {

    @Test
    void doScrape() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String result = webScrapingTool.doScrape("https://www.bilibili.com");
        Assertions.assertNotNull(result);
    }

}