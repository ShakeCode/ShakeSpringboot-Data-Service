/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.data.model.easyexcel.User;
import com.data.service.custer.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The type User data listener.
 */
public class UserDataListener extends AnalysisEventListener<User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataListener.class);

    private UserService userService;

    /**
     * Instantiates a new User data listener.
     * @param userService the user service
     */
    public UserDataListener(UserService userService) {
        this.userService = userService;
    }

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    private static final List<User> list = new ArrayList<>();

    /**
     * Invoke.
     * @param data    the data
     * @param context the context
     */
    @Override
    public void invoke(User data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    /**
     * Do after all analysed.
     * @param context the context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        if (!CollectionUtils.isEmpty(list)) {
            // userService.saveBatch(list);
        }
        LOGGER.info("存储数据库成功！");
    }
}
