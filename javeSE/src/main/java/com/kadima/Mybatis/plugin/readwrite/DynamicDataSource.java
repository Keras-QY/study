package com.kadima.Mybatis.plugin.readwrite;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * key : READ --> value : readDataSource
     * key : WRITE --> value : writeDataSource
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceType().name();
    }
}