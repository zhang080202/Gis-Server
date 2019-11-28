package com.gyxt.gis.service;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 
 * @author zhangyf
 * @version 2.1.0 
 * 对sql 查询进行优化
 *
 */
@Service
public class GisServerService {
	
	private static final Logger logger = LoggerFactory.getLogger(GisServerService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public byte[] show(String layer, Integer column, Integer row, Integer level) {
		//添加图层算法 20191024
		row = (int) (Math.pow(2, level) - row) - 1;
		
		Instant start = Instant.now();
		byte[] result = executeQuery( new Object[] {column, row, level});
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		logger.info("wast time is {} ms", duration.toMillis());
		
		return result;
	}
	
	private byte[] executeQuery(Object[] args) {
		String sql = "SELECT tile_id FROM map WHERE tile_column = ? and tile_row = ? and zoom_level = ?";
		String tileId = jdbcTemplate.queryForObject(sql, args, String.class);
		
		String sql1 = "SELECT tile_data FROM images WHERE tile_id = '" + tileId + "'";
		
		return jdbcTemplate.queryForObject(sql1, (rs, rowNum) -> rs.getBytes(1));
	}
	
}
