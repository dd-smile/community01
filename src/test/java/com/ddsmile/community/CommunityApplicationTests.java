package com.ddsmile.community;


import com.ddsmile.dao.*;
import com.ddsmile.entity.*;
import com.ddsmile.mqtt.Callback;
import com.ddsmile.util.DeleteRubbish;
import com.ddsmile.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试类
 */
@SpringBootTest
class CommunityApplicationTests {

//----------------------------------------------- 用户表与帖子表 数据访问层的测试

	@Resource
	private DataWatchMapper dataWatchMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private DiscussPostMapper discussPostMapper;
	@Resource
	private DataSensorMapper dataSensorMapper;

	@Test
	public void testSelectUser(){
		User user = userMapper.selectById(101);
		System.out.println(user);
	}

	@Test
	public void testSelectPosts(){
		List<DiscussPost> list = discussPostMapper.selectDiscussPosts(101, 0, 10);
		list.forEach(discussPost -> System.out.println(discussPost) );

		int i = discussPostMapper.selectDiscussPostRows(101);
		System.out.println(i);
	}

	@Test
	public void testSelectUser01(){
		List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
		for (DiscussPost discussPost : list){
			System.out.println(discussPost);
		}
	}
//-----------------------------------------------发送邮件的测试
	@Autowired
	private MailClient mailClient;

	@Autowired
	private TemplateEngine templateEngine;

	@Test
	public void testTextMail(){
		mailClient.sendMail("892969403@qq.com","TEST", "你好,这里是肯德基疯狂星期四,速速v我50");
	}

	@Test
	public void testHtmlMail(){
		Context context = new Context();
		context.setVariable("username","huohuohuo");//可以动态设置用户名
		String content = templateEngine.process("/mail/activation",context);
		System.out.println(content);
		mailClient.sendMail("892969403@qq.com","Html",content);
	}
//-------------------------------------------------清除数据库垃圾的测试

	@Test
	public void testDelete(){
		userMapper.deleteUser(150);
	}


//------------------------------------------------节点数据数据访问层测试

	//测试增加
	@Test
	public void testInsertDataWatch(){
		DataWatch dataWatch = new DataWatch();
		dataWatch.setDeviceId(1);
		dataWatch.setSensorName("温度");
		dataWatch.setSensorTag("温湿度传感器");
		dataWatch.setSensorVar(20);
		dataWatch.setRecordTime(new Date(System.currentTimeMillis() + 1000*60*10));
		dataWatchMapper.insertDataWatch(dataWatch);
	}
	//测试查询
	@Test
	public void testSelectDataById(){
		DataWatch dataWatch = dataWatchMapper.selectDataById(1);
		System.out.println(dataWatch);
	}

	@Test
	public void getDataMapTest(){
		Map<String,Object> map_test = new HashMap<>();
		Callback callback = new Callback();
		System.out.println(map_test.get("temp"));
		System.out.println(map_test.get("hum"));
		System.out.println(map_test.get("co2"));
	}

	//测试增加DataSensor数据
	@Test
	public void testInsertDataSensor(){
		DataSensor dataSensor = new DataSensor();
		dataSensor.setSensorTemp("29");
		dataSensor.setSensorHum("44");
		dataSensor.setSensorCo2("420");
		dataSensor.setRecordTime(new Date(System.currentTimeMillis() + 1000*60*10));
		dataSensorMapper.insertDataSensor(dataSensor);
	}
	//测试查询最新一条DataSensor数据
	@Test
	public void testSelectDataSensor(){
		DataSensor dataSensor = dataSensorMapper.selectDataByTime();
		System.out.println(dataSensor);
	}

//------------------------------------------------- 用户登录凭证表 数据访问层的测试

	@Autowired
	private LoginTicketMapper loginTicketMapper;
	//测试增加
	@Test
	public void test3(){
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(101);
		loginTicket.setTicket("abc");
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*60*10));
		loginTicketMapper.insertLoginTicket(loginTicket);
	}

	//测试查询、修改
	@Test
	public void test4(){
		LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
		System.out.println(loginTicket);

		loginTicketMapper.updateStatus("abc",1);
		loginTicket = loginTicketMapper.selectByTicket("abc");
		System.out.println(loginTicket);
	}

//------------------------------------------------- 评论表 数据访问层的测试
	@Autowired
	private CommentMapper commentMapper;

	//测试查询
	@Test
	public void test01(){
		int i = commentMapper.selectCountByEntity(1, 228);
		System.out.println(i);
	}

//------------------------------------------------------------------------普通测试
	//查看springboot版本
	@Test
	public void test001(){
		String springVersion = SpringVersion.getVersion();
		String springBootVersion = SpringBootVersion.getVersion();
		System.out.println("Spring版本:"+springVersion+"\nSpringBoot版本:"+springBootVersion);
	}

}
