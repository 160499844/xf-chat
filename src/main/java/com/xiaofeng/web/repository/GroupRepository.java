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
import com.xiaofeng.netty.server.handler.TextWebSocketFrameHandler;
import com.xiaofeng.utils.exception.BaseException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class GroupRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 创建对象
	 * 
	 * @param user
	 */
	public void saveGroup(Group group) {
		mongoTemplate.save(group);
	}

	/**
	 * @Title: findByGroupId @Description: 根据组id查询群聊信息 @param: @param
	 * groupId @param: @return @return: Group @throws
	 */
	public Group findByGroupId(String groupId) {
		Query query = new Query(Criteria.where("groupId").is(groupId));
		List<Group> find = mongoTemplate.find(query, Group.class);
		if (find.size()>0) {
			return find.get(0);
		} else {
			throw new BaseException("小组不存在!");
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
	public List<Group> findByKey(String key,String value) {
		Query query = new Query(Criteria.where(key).is(value));
		List<Group> find = mongoTemplate.find(query, Group.class);
		return find;
	}
	/**
	 * 
	 * @Title: editCurrentCount   
	 * @Description: 更新小组人数  
	 * @param: @param groupId
	 * @param: @param count      
	 * @return: void      
	 * @throws
	 */
	public void editCurrentCount(String groupId,Integer count) {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupId").is(groupId));
		Update update = new Update();
		update.set("currentCount", count);
		mongoTemplate.upsert(query, update, "chat_group");
	}
	/**
	 * 
	 * @Title: clearGroups   
	 * @Description: 清除全部群组信息
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void clearGroups() {
		Query query = Query.query(Criteria.where("createDt").lt(new Date()));
		List<Group> findAllAndRemove = mongoTemplate.findAllAndRemove(query, Group.class);
		log.info("执行清除数据!");
		for (Group group : findAllAndRemove) {
			log.info(group.toString());
		}
		log.info("清空全部群组信息完毕!");
	}
}
