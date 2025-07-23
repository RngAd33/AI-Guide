package com.rngad33.aiguide.tools;

import cn.hutool.core.io.FileUtil;
import com.rngad33.aiguide.constant.FilePathConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 文件操作工具（提供文件读写功能）
 */
public class FileOperatingTool {

    private final String FILE_DIR = FilePathConstant.FILE_SAVE_PATH + "/file";

    /**
     * 文件读取
     *
     * @param fileName
     * @return
     */
    @Tool(description = "Read content from a file")
    public String doRead(@ToolParam(description = "Name of a file to read") String fileName
    ) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    /**
     * 文件写入
     *
     * @param fileName
     * @param content
     * @return
     */
    @Tool(description = "Write content from a file")
    public String doWrite(@ToolParam(description = "Name of a file to write") String fileName,
                          @ToolParam(description = "Content to write to the file") String content
    ) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return ">>>Write successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }


}