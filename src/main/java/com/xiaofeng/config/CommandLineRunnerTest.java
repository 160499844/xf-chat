package com.xiaofeng.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.XfChatApplication;
import com.xiaofeng.utils.SpringBeanUtil;
import com.xiaofeng.web.repository.GroupRepository;
import com.xiaofeng.web.repository.UserRepository;

/**
 * 初始化
 * @author xiaofeng
 *
 */
@Component
public class CommandLineRunnerTest implements CommandLineRunner {


    @Override
    public void run(String... strings) throws Exception {
    	init();
    }
    
    /**
	 * 
	 * @Title: init   
	 * @Description: 初始化
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public static void init() {
		//清空MongoDB群组数据
		GroupRepository bean = SpringBeanUtil.getBean(GroupRepository.class);
		bean.clearGroups();
		UserRepository userRepository = SpringBeanUtil.getBean(UserRepository.class);
		userRepository.clearUsers();
	}
	
}
