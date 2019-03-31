package com.data.controller;

import com.data.annotation.DS;
import com.data.dao.custer.SongDao;
import com.data.dao.master.AdminDao;
import com.data.dbConfig.DataSourceContextHolder;
import com.data.dbConfig.DataSourceEnum;
import com.data.dbConfig.DynamicDataSource;
import com.data.service.custer.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Api(value="单元测试相关的api",description = "单元测试相关的api")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private SongDao songDao;

    @Autowired
    private UserService userService;

    @Resource
    DataSource dataSource;

    @ApiOperation(value = "获取所有管理员")
    @GetMapping("api/admin/all")
    @DS(DataSourceEnum.master)
    List<Map<String,Object>> getAdmin() throws SQLException {
        int i = 1/0;
        logger.debug("Use DataSource :{} >", DataSourceContextHolder.getDB());
        System.out.println("数据源>>>>>>" + dataSource.getClass());
        Connection connection = dataSource.getConnection();

        System.out.println("连接>>>>>>>>>" + connection);
        System.out.println("连接地址>>>>>" + connection.getMetaData().getURL());
        connection.close();

        return adminDao.getAllAdmin();
    }

    @ApiOperation(value = "获取所有用户")
    @GetMapping("api/user/all")
    @DS(DataSourceEnum.custer)
    List<Map<String,Object>> getUser() throws SQLException {
        System.out.println("数据源>>>>>>" + dataSource.getClass());
        Connection connection = dataSource.getConnection();

        System.out.println("连接>>>>>>>>>" + connection);
        System.out.println("连接地址>>>>>" + connection.getMetaData().getURL());
        connection.close();

        ConcurrentHashMap<Object,Object> dataSourceConcurrentHashMap = DynamicDataSource.targetDataSourceMap;

        System.out.println("-----------------------》dataSourceConcurrentHashMap"+dataSourceConcurrentHashMap);
        return userService.getUser();
    }

}
