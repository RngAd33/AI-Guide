package com.rngad33.aiguide.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TerminalOperatingToolTest {

    @Test
    void doTerminalOperating() {
        TerminalOperatingTool terminalOperatingTool = new TerminalOperatingTool();
        String command = "ollama list";
        String result = terminalOperatingTool.doTerminalOperating(command);
        Assertions.assertNotNull(result);
    }

}