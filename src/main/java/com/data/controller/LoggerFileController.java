package com.data.controller;

import com.data.service.master.LoggerFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * The type Logger file controller.
 */
@Api(value = "搜索日志文件内容")
@RequestMapping(value = "/v1/log/")
@RestController
public class LoggerFileController {
    private final LoggerFileService loggerFileService;

    /**
     * Instantiates a new Logger file controller.
     * @param loggerFileService the logger file service
     */
    public LoggerFileController(LoggerFileService loggerFileService) {
        this.loggerFileService = loggerFileService;
    }

    /**
     * Gets admin.
     * @return the admin
     * @throws SQLException the sql exception
     */
    @ApiOperation(value = "模糊搜索日志内容")
    @GetMapping("query")
    public String queryLogContent(@RequestParam("word") String word) {
        return loggerFileService.queryLogContent(word);
    }
}
