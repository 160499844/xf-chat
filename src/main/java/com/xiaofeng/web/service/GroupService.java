package com.xiaofeng.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaofeng.entity.Group;
import com.xiaofeng.web.repository.GroupRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class GroupService {

	@Autowired
	private GroupRepository groupRepository;
	
	/**
	 * 
	 * @Title: getGroupCount   
	 * @Description: 获取小组成员数量
	 * @param: @param groupId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 */
	public Integer getGroupCount(String groupId) {
		//Group group = GroupContext.GROUPS.get(groupId);
		Group group = groupRepository.findByGroupId(groupId);
		if(group==null) {
			return 0;
		}
		Integer currentCount = group.getCurrentCount();
		return currentCount;
	}
	
	/**
	 * 
	 * @Title: groupReduce   
	 * @Description: 减少组成员数量
	 * @param: @param groupId      
	 * @return: void      
	 * @throws
	 */
	public Integer groupReduceCount(String groupId) {
		Group group = groupRepository.findByGroupId(groupId);
		if(group==null) {
			return 0;
		}
		Integer groupCount = group.getCurrentCount();
		if(groupCount!=null && groupCount  > 0 ) {
			groupCount = groupCount - 1;
			groupRepository.editCurrentCount(groupId, groupCount);
		}
		return groupCount;
	}
	/**
	 * 
	 * @Title: groupAddCount   
	 * @Description: 增加组成员数量 
	 * @param: @param groupId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 */
	public Integer groupAddCount(String groupId) {
		Group group = groupRepository.findByGroupId(groupId);
		Integer groupCount = group.getCurrentCount();
		if(groupCount!=null) {
			groupCount = groupCount + 1;
		}else {
			groupCount = 1;
		}
		groupRepository.editCurrentCount(groupId, groupCount);
		return groupCount;
	}
}
