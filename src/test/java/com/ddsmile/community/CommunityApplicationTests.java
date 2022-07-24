package com.ddsmile.community;


import com.ddsmile.dao.DiscussPostMapper;
import com.ddsmile.dao.LoginTicketMapper;
import com.ddsmile.dao.UserMapper;
import com.ddsmile.entity.DiscussPost;
import com.ddsmile.entity.LoginTicket;
import com.ddsmile.entity.User;
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
import java.util.List;

/**
 * 测试类
 */
@SpringBootTest
class CommunityApplicationTests {

//----------------------------------------------- 用户表与帖子表 数据访问层的测试
	@Resource
	private UserMapper userMapper;
	@Resource
	private DiscussPostMapper discussPostMapper;

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

//------------------------------------------------------------------------普通测试
	//查看springboot版本
	@Test
	public void test001(){
		String springVersion = SpringVersion.getVersion();
		String springBootVersion = SpringBootVersion.getVersion();
		System.out.println("Spring版本:"+springVersion+"\nSpringBoot版本:"+springBootVersion);
	}

}
