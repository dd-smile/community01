package com.ddsmile.community;


import com.ddsmile.dao.DiscussPostMapper;
import com.ddsmile.dao.UserMapper;
import com.ddsmile.entity.DiscussPost;
import com.ddsmile.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试类
 */
@SpringBootTest
class CommunityApplicationTests {

//-----------------------------------------------数据访问层的测试
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

}
