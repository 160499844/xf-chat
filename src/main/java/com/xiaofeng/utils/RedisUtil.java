package com.xiaofeng.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis工具类
 * 
 * @author Wayne
 * @date 2019/4/30
 */
@Component
public class RedisUtil {

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 向Redis中存值，永久有效
	 */
	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			return "0";
		} finally {
			returnResource(jedisPool, jedis);
		}
	}
	/**
	 * 向Redis中存值,设置过期时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public String set(String key, String value,int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.setex(key, seconds, value);
		} catch (Exception e) {
			return "0";
		} finally {
			returnResource(jedisPool, jedis);
		}
	}
	/**
	 * 根据传入Key获取指定Value
	 */
	public String get(String key) {
		Jedis jedis = null;
		String value;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			return "0";
		} finally {
			returnResource(jedisPool, jedis);
		}
		return value;
	}

	/**
	 * 校验Key值是否存在
	 */
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			return false;
		} finally {
			returnResource(jedisPool, jedis);
		}
	}

	/**
	 * 删除指定Key-Value
	 */
	public Long delete(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			return 0L;
		} finally {
			returnResource(jedisPool, jedis);
		}
	}

	// 以上为常用方法，更多方法自行百度

	/**
	 * 释放连接
	 */
	private static void returnResource(JedisPool jedisPool, Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
}
