package com.data.dbConfig;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Yml multiple data source provider.
 */
@Configuration
public class YmlMultipleDataSourceProvider implements MultipleDataSourceProvider {
    private final MultipleDSConfig multipleDSConfig;

    /**
     * Instantiates a new Yml multiple data source provider.
     * @param multipleDSConfig the multiple ds config
     */
    public YmlMultipleDataSourceProvider(MultipleDSConfig multipleDSConfig) {
        this.multipleDSConfig = multipleDSConfig;
    }

    /**
     * Load data source map.
     * @return the map
     */
    @Override
    public Map<String, DataSource> loadDataSource() {
        Map<String, Map<String, String>> configDs = multipleDSConfig.getDs();
        Map<String,DataSource> map = new HashMap<>(configDs.size());
        try{
            for (String key: configDs.keySet()){
                DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(configDs.get(key));
                map.put(key, multipleDSConfig.buildDataSource(druidDataSource));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
