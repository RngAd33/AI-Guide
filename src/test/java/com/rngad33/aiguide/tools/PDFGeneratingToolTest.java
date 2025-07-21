package com.rngad33.aiguide.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PDFGeneratingToolTest {

    @Test
    void generatePDF() {
        String fileName = "test.pdf";
        String content = "This is a test PDF.";
        PDFGeneratingTool tool = new PDFGeneratingTool();
        String result = tool.generatePDF(fileName, content);
        assertTrue(result.contains("PDF generated successfully"));
    }

}