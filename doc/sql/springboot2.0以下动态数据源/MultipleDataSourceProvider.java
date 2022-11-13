package com.data.dbConfig;

import javax.sql.DataSource;
import java.util.Map;

/**
 * The interface Multiple data source provider.
 */
public interface MultipleDataSourceProvider {
    /**
     * Load data source map.
     * @return the map
     */
    Map<String, DataSource> loadDataSource();
}
