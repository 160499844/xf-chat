package com.xiaofeng.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.xiaofeng.global.UtilConstants;
/**
 * 设置redis中session过期时间
 * @author xiaofeng
 *
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = UtilConstants.SESSION_TIMEOUT)
public class RedisSessionConfig {

}
