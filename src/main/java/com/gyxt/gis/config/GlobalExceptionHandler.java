package com.gyxt.gis.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gyxt.gis.exception.InvalidLayerException;

/**
 * 统一异常处理
 * @author zhangyf
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public Map<String, Object> handler(Exception e) {
		Map<String, Object> result = new HashMap<>();
		int code;
		String msg = null;
		if (e instanceof InvalidLayerException) {
			code = 500;
			msg = e.getMessage();
		} else if (e instanceof EmptyResultDataAccessException) {
			msg = "未查询到数据";
			code = 404;
		}else {
			code = 500;
			msg = "系统异常";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		result.put("message", msg);
		result.put("code", code);
		return result;
	}

}
