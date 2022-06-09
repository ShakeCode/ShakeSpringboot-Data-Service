package com.data.dbConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataSourceContextHolder {
    public static final Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);

    /**
     * 默认数据源
     */
    public static final String DEFAULT_DS = DataSourceEnum.master.name();

//    private static final ThreadLocal<DataSourceEnum> CONTEXT_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>() {

        @Override
        protected String initialValue() {
            return DataSourceEnum.master.name();
        }
    };
        /*
     * 管理所有的数据源id;
     * 主要是为了判断数据源是否存在;
     */
    public static List<String> dataSourceIds = new ArrayList<String>();

    // 设置数据源名
    public static void setDB(String dbType) {
        log.debug("切换到{}数据源", dbType);
        CONTEXT_HOLDER.set(dbType);
    }

    // 获取数据源名
    public static String getDB() {
        return CONTEXT_HOLDER.get();
    }

    // 清除数据源名
    public static void clearDB() {
        CONTEXT_HOLDER.remove();
    }

    public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }
}
