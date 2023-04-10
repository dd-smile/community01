package com.ddsmile.community;

import com.alibaba.fastjson.JSONObject;
import com.ddsmile.dao.DiscussPostMapper;
import com.ddsmile.dao.MessageMapper;
import com.ddsmile.entity.DiscussPost;
import com.ddsmile.entity.Message;
import com.ddsmile.entity.Scheduled;
import com.ddsmile.service.ScheduledService;
import com.ddsmile.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 测试类
 */
@SpringBootTest
public class Test1 {

//------------------------------------------------测试敏感词

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private ScheduledService scheduledService;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testSensitiveFilter(){
        String text = "这里面可以赌^^^博，可以嫖^^^娼、可以开^^^票哈哈哈！";
        String text2 = "这里面可以赌博，可以嫖娼、可以开票哈哈哈！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

//------------------------------------------------测试定时任务业务
    @Test
    public void testScheduled(){

        scheduledService.updateCronBy("1", "*/5 * * * * ?");

        Scheduled scheduled = scheduledService.findCronById("1");
        System.out.println(scheduled);

    }


//------------------------------------------------测试私信列表 数据访问层
    @Test
    public void testSelectLetters(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        messages.forEach(message -> System.out.println(message));

        int i = messageMapper.selectConversationCount(111);
        System.out.println(i);

        List<Message> messages1 = messageMapper.selectLetters("111_112", 0, 10);
        messages1.forEach(message -> System.out.println(message));

        int i1 = messageMapper.selectLetterCount("111_112");
        System.out.println(i1);

        int i2 = messageMapper.selectLettersUnreadCount(131, "111_131");
        System.out.println(i2);
    }

    @Test
    public void testJson(){
        String s = "{\"Temp\":19,\"Hum\":44,\"Co2\":400}";
        JSONObject jsonObject = JSONObject.parseObject(s);
        String temp = jsonObject.getString("Temp");
        System.out.println(temp);
    }

}
