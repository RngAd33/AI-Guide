package com.rngad33.aiguide.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResourceDownloadToolTest {

    @Test
    void doDownload() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String result = resourceDownloadTool.doDownload("https://www.codefather.cn/logo.png", "logo.png");
        Assertions.assertNotNull(result);
    }

}