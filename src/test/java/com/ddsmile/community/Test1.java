package com.ddsmile.community;

import com.ddsmile.dao.DiscussPostMapper;
import com.ddsmile.entity.DiscussPost;
import com.ddsmile.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 测试敏感词
 */
@SpringBootTest
public class Test1 {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testSensitiveFilter(){
        String text = "这里面可以赌^^^博，可以嫖^^^娼、可以开^^^票哈哈哈！";
        String text2 = "这里面可以赌博，可以嫖娼、可以开票哈哈哈！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }




}
