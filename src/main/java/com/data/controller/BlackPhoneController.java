package com.data.controller;

import com.data.annotation.DS;
import com.data.dbConfig.DataSourceEnum;
import com.data.model.ResultVO;
import com.data.service.master.BlackPhoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Api(value = "黑名单业务层", description = "黑名单业务层")
@RequestMapping("/api/v1/black")
public class BlackPhoneController {
    @Autowired
    private BlackPhoneService blackPhoneService;

    @ApiOperation(value = "导入黑名单")
    @PostMapping("/import")
    @DS(DataSourceEnum.master)
    ResultVO importBlack(@RequestParam(name = "file") MultipartFile file) {
        return blackPhoneService.importBlack();
    }

    @ApiOperation(value = "导出黑名单")
    @GetMapping("/export")
    public void exportBlack() throws IOException {
        // blackPhoneService.exportOpenCsvFile();
        blackPhoneService.exportSXSSFWorkbookFile();
        // blackPhoneService.exportCsvStream();
    }
}
