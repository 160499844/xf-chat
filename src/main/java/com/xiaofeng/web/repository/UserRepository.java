package com.xiaofeng.web.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ExecutableRemoveOperation.ExecutableRemove;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;
import com.xiaofeng.entity.Group;
import com.xiaofeng.entity.UserEntity;
import com.xiaofeng.netty.server.handler.TextWebSocketFrameHandler;
import com.xiaofeng.utils.exception.BaseException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class UserRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 创建对象
	 * 
	 * @param user
	 */
	public void saveUser(UserEntity group) {
		mongoTemplate.save(group);
	}

	/**
	 * @Title: findByGroupId 
	 * @Description: 根据组id查询群聊信息 
	 * @param: @param
	 * groupId @param: @return 
	 * @return: Group @throws
	 */
	public UserEntity findByUserId(String userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		List<UserEntity> find = mongoTemplate.find(query, UserEntity.class);
		if (find.size()>0) {
			return find.get(0);
		} else {
			throw new BaseException("用户["+userId+"]不存在!");
		}
	}
	/**
	 * 
	 * @Title: findByKey   
	 * @Description: 根据字段值查询多条记录
	 * @param: @param key
	 * @param: @param value
	 * @param: @return      
	 * @return: Group      
	 * @throws
	 */
	public List<UserEntity> findByKey(String key,String value) {
		Query query = new Query(Criteria.where(key).is(value));
		List<UserEntity> find = mongoTemplate.find(query, UserEntity.class);
		return find;
	}
	/**
	 * 
	 * @Title: deleteUser   
	 * @Description: 删除用户信息
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public List<UserEntity> deleteUser(String userId) {
		Query query = Query.query(Criteria.where("userId").is(userId));
		List<UserEntity> findAllAndRemove = mongoTemplate.findAllAndRemove(query, UserEntity.class);
		for (UserEntity group : findAllAndRemove) {
			log.info(group.toString());
			log.info("删除用户"+group.getUserName()+"信息完毕!");
		}
		return findAllAndRemove;
	}
	/**
	 * 
	 * @Title: editCol   
	 * @Description: 更新用户信息
	 * @param: @param userId
	 * @param: @param key
	 * @param: @param value      
	 * @return: void      
	 * @throws
	 */
	public void editCol(String userId,String key,String value) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Update update = new Update();
		update.set(key, value);
		mongoTemplate.upsert(query, update, "chat_user");
	}
	
	 /* 
	 * @Title: clearGroups   
	 * @Description: 清除全部用户信息
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void clearUsers() {
		Query query = Query.query(Criteria.where("createDt").lt(new Date()));
		List<UserEntity> findAllAndRemove = mongoTemplate.findAllAndRemove(query, UserEntity.class);
		for (UserEntity group : findAllAndRemove) {
			log.info(group.toString());
		}
		log.info("用户数据初始化!");
	}
}
