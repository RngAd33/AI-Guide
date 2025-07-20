package com.rngad33.aiguide.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Resource
    private FileOperationTool fileOperationTool;

    @Test
    void doRead() {
        String result = fileOperationTool.doRead("test.txt");
        Assertions.assertNotNull(result);
    }

    @Test
    void doWrite() {
        String result = fileOperationTool.doWrite("test.txt", "Hello World!");
        Assertions.assertNotNull(result);
    }

}