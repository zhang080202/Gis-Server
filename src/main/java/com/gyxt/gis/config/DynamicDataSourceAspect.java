package com.gyxt.gis.config;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 多数据源切面
 * 
 * @author zhangyf
 *
 */
@Aspect
@Component
public class DynamicDataSourceAspect {
	
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("execution( * com.gyxt.gis.service..*.*(String, ..))")
    public void serviceAspect() {}

    @Before("serviceAspect()")
    public void switchDataSource(JoinPoint point) {
    	Object[] params = point.getArgs();
    	String param = (String) params[0];
    	
    	if(StringUtils.isBlank(param)) return;
    	
    	if(DynamicRoutingDataSource.isExistDataSource(param) && !param.equals(DynamicDataSourceContextHolder.getDataSourceKey()) ) {
    		DynamicDataSourceContextHolder.setDataSourceKey(param);
    	}
    		
        logger.info("Switch DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }

    /**
     * 重置数据源
     * 
     * @param point
     */
    @After("serviceAspect()")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        logger.info("Restore DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }

}
