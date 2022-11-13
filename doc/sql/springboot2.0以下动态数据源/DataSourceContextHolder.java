package com.data.dbConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Data source context holder.
 */
public class DataSourceContextHolder {
    /**
     * The constant LOGGER.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);

    /**
     * 默认数据源
     */
    public static final String DEFAULT_DS = DataSourceEnum.master.name();

    private static final ThreadLocal<String> CONTEXT_HOLDER = new InheritableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return DataSourceEnum.master.name();
        }
    };

    /**
     * Sets db.   设置数据源名
     * @param dbType the db type
     */
    public static void setDB(String dbType) {
        LOGGER.debug("切换到{}数据源", dbType);
        CONTEXT_HOLDER.set(dbType);
    }

    /**
     * Gets db.   获取数据源名
     * @return the db
     */
    public static String getDB() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * Clear db. 清除数据源名
     */
    public static void clearDB() {
        CONTEXT_HOLDER.remove();
    }
}
