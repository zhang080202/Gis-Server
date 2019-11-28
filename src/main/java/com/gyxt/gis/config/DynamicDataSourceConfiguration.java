package com.gyxt.gis.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import com.gyxt.gis.exception.InvalidLayerException;

/**
 * 动态数据源配置类
 * @author zhangyf
 * 2019年11月10日
 */
@Configuration
@ConditionalOnBean( {Environment.class} )
@ConditionalOnClass(DynamicRoutingDataSource.class)
public class DynamicDataSourceConfiguration {
	
	@Autowired
	private Environment env;
	
	private final String PREFIX_DATASOURCE = "spring.datasource.";
	
	private final String SUFFIX_DATASOURCE_URL = ".url";
	
	private final String DATASOURCE_DRIVERCLASSNAME = "org.sqlite.JDBC";
	
	@Bean(name = {"datasources"})
	public Map<Object, Object> dynamicDataSourceInit() {
		Map<Object, Object> datasources = new HashMap<>();
		String layers = env.getProperty("layers");
		if (StringUtils.isBlank(layers)) {
			throw new InvalidLayerException("layers is empty!");
		}
		String[] layersArr = StringUtils.split(layers, ",");
		
		for (String layer : layersArr) {
			layer = layer.trim();
			String url = env.getProperty(PREFIX_DATASOURCE + layer + SUFFIX_DATASOURCE_URL);
			SQLiteDataSource dataSource = DataSourceBuilder.create()
													       .driverClassName(DATASOURCE_DRIVERCLASSNAME)
													       .url(url)
													       .type(SQLiteDataSource.class)
													       .build();
			datasources.put(layer, dataSource);
		}
		
		return datasources;
	}
	
	@Bean
	@ConditionalOnBean(name = "datasources")
	public DynamicRoutingDataSource dynamicRoutingDataSource(@Qualifier("datasources") Map<Object, Object> datasources) {
		DynamicRoutingDataSource source = new DynamicRoutingDataSource();
		source.setTargetDataSources(datasources);
		return source;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(@Qualifier("dynamicRoutingDataSource") DynamicRoutingDataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
}
