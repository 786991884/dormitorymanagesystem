package test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.support.json.JSONUtils;

import cn.edu.sxau.dormitorymanage.model.Tuser;
import cn.edu.sxau.dormitorymanage.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml", "classpath:spring-hibernate.xml" })
public class TestInitService {

	@Autowired
	private UserService userService;

	@Test
	public void initDb() {
	}

	@Test
	@Transactional(readOnly = true)
	public void getUser() {
		Tuser t = userService.getById("0");
		System.out.println(JSONUtils.toJSONString(t));
	}

}
