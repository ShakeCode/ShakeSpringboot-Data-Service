/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.data.model.easyexcel.EmployeesEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Employees listener.
 */
public class EmployeesListener extends AnalysisEventListener<EmployeesEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesListener.class);

    /**
     * 这个每一条数据解析都会来调用
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context the context
     */
    @Override
    public void invoke(EmployeesEntity data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
    }

    /**
     * 所有数据解析完成了 都会来调用
     * @param context the context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("准备入库...");
    }
}
