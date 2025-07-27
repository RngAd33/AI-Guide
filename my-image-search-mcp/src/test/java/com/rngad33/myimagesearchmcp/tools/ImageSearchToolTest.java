package com.rngad33.myimagesearchmcp.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void doSearch() {
        String result = imageSearchTool.doSearch("caffe");
        Assertions.assertNotNull(result);
    }

}