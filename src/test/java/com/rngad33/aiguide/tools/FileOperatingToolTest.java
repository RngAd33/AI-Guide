package com.rngad33.aiguide.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileOperatingToolTest {

    @Resource
    private FileOperatingTool fileOperatingTool;

    @Test
    void doRead() {
        String result = fileOperatingTool.doRead("test.txt");
        Assertions.assertNotNull(result);
    }

    @Test
    void doWrite() {
        String result = fileOperatingTool.doWrite("test.txt", "Hello World!");
        Assertions.assertNotNull(result);
    }

}