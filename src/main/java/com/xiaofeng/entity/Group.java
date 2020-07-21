package com.xiaofeng.entity;

import lombok.Data;

/**
 * 
 * @ClassName:  Group   
 * @Description:群组对象
 * @author: 小峰
 * @date:   2020年7月21日 下午3:48:07
 */
@Data
public class Group {

	private String groupId;//组标识
	private String groupName;//组名称
	private Integer max = 999;//最大成员数量
	private Integer currentCount = 0;//当前成员数量
	
	private GroupToken token;//权限校验字段，敏感
	
}
