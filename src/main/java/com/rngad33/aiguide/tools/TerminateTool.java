package com.rngad33.aiguide.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 任务终止工具
 */
public class TerminateTool {

    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate() {
        return "任务已终止";
    }

}