/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.model.easyexcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    // 主键id
    // 生成报表时忽略，不生成次字段
    @ExcelIgnore
    private Integer id;

    // 定义表头名称和位置,0代表第一列
    @ExcelProperty(value = "name", index = 0)
    private String name;

    @ExcelProperty(value = "age", index = 1)
    private Integer age;

    // 定义列宽
    @ColumnWidth(20)
    @DateTimeFormat(value = "yyyy/MM/dd")
    @ExcelProperty(value = "birthday", index = 2)
    private Date birthday;
}
