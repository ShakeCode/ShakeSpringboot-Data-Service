/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The type Jdbc druid utils.
 */
public class JdbcDruidUtils {
    private static DataSource ds;

    //优化使用Druid数据库连接池工具类
    //加载配置文件，在访问该类时自动加载
    static {
        try {
            //1.创建properties对象
            Properties pro = new Properties();
            //2.读取配置文件
            InputStream rs = JdbcDruidUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            //3.加载配置文件
            pro.load(rs);
            //4.获取DataSource连接池对象
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets connection.获取数据库连接
     * @return the connection
     */
    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Close. 关闭资源
     * @param statement  the statement
     * @param connection the connection
     */
    public static void close(Statement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}

