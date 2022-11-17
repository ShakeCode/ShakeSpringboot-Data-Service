/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.model.easyexcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * The type User salary.
 */
@Data
public class UserSalary {
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("日期")
    private Date date;

    @ExcelProperty("薪资")
    private Double salary;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String sex;
}
