package com.rngad33.aiguide.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.rngad33.aiguide.constant.FilePathConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * 资源下载工具
 */
public class ResourceDownloadTool {

    @Tool(description = "Download source")
    public String doDownload(@ToolParam(description = "URL of the resource to download") String url,
                             @ToolParam(description = "Name of the file to save the downloaded resource") String fileName
    ) {
        String fileDir = FilePathConstant.FILE_SAVE_PATH + "/download";
        String filePath = fileDir + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 使用 Hutool 的 downloadFile 方法下载资源
            HttpUtil.downloadFile(url, new File(filePath));
            return "Resource downloaded successfully to: " + filePath;
        } catch (Exception e) {
            return "Error downloading resource: " + e.getMessage();
        }
    }

}