package com.data.controller;

import com.alibaba.excel.EasyExcel;
import com.data.easyexcel.EmployeesListener;
import com.data.easyexcel.UserDataListener;
import com.data.model.ResultVO;
import com.data.model.easyexcel.EmployeesEntity;
import com.data.model.easyexcel.User;
import com.data.model.easyexcel.UserSalary;
import com.data.service.custer.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Easy excel test controller.
 */
@RequestMapping("/v1/")
@RestController
@Api(value = "easy-excel-单元测试相关的api", description = "easy-excel-单元测试相关的api")
public class EasyExcelTestController {
    @Autowired
    private UserService userService;

    @Value("${excel.path:/excel/}")
    private String path;

    /**
     * Import employee result vo.
     * @return the result vo
     * @throws SQLException the sql exception
     */
    @ApiOperation(value = "导入excel")
    @PostMapping("/employee/import")
    public ResultVO<String> importEmployee() throws SQLException {
        File file = new File("./src/main/resources/大客户部-薪酬表.xlsx");
        String absolutePath = file.getAbsolutePath();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(absolutePath, EmployeesEntity.class, new EmployeesListener()).sheet().doRead();
        return ResultVO.success();
    }

    /**
     * Simple write.
     */
    @ApiOperation(value = "导出excel")
    @PostMapping("/employee/export")
    public void simpleWrite() {
        // 写
        File file = new File("./src/main/resources/DemoData.xlsx");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        List<UserSalary> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserSalary data = new UserSalary();
            data.setName("黎明" + i);
            data.setDate(new Date());
            data.setSalary(0.56);
            list.add(data);
        }
        EasyExcel.write(file.getAbsolutePath(), UserSalary.class).sheet("薪资").doWrite(list);
    }

    /**
     * Read 4 file string.
     * @return the string
     * @throws IOException the io exception
     */
    @PostMapping("/user/import")
    @ResponseBody
    public String read4File() throws IOException {
        File file = new File("./src/main/resources/用户表导入.xlsx");
        EasyExcel.read(file, User.class, new UserDataListener(userService)).sheet().doRead();
        return "读取成功";
    }

    /**
     * Upload string.
     * @param file the file
     * @return the string
     * @throws IOException the io exception
     */
    @ApiOperation(value = "表单文件-上传excel")
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), User.class, new UserDataListener(userService)).sheet().doRead();
        return "上传成功";
    }

}
