package com.gyxt.gis.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源路由配置类
 * 
 * @author zhangyf
 *
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static Map<Object, Object> targetDataSources = new HashMap<>();
    
    /**
     * 设置当前数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSourceKey());
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
    
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
    	super.setTargetDataSources(targetDataSources);
		DynamicRoutingDataSource.targetDataSources = targetDataSources;
	}
    
    /**
     * 是否存在当前key的 DataSource
     * 
     * @param key
     * @return 存在返回 true, 不存在返回 false
     */
    public static boolean isExistDataSource(String key) {
    	return targetDataSources.containsKey(key);
    }
    
}
