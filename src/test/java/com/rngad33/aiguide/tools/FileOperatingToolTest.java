package com.rngad33.aiguide.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileOperatingToolTest {

    @Test
    void doRead() {
        FileOperatingTool fileOperatingTool = new FileOperatingTool();
        String result = fileOperatingTool.doRead("test.txt");
        Assertions.assertNotNull(result);
    }

    @Test
    void doWrite() {
        FileOperatingTool fileOperatingTool = new FileOperatingTool();
        String result = fileOperatingTool.doWrite("test.txt", "Hello World!");
        Assertions.assertNotNull(result);
    }

}