package com.ddsmile.controller;

import com.ddsmile.entity.DiscussPost;
import com.ddsmile.entity.Page;
import com.ddsmile.entity.User;
import com.ddsmile.service.DiscussPostService;
import com.ddsmile.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页视图层
 * 完成的功能是 : 查询用户所发表的帖子显示在首页中
 */
@Controller
public class HomeController {
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private UserService userService;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //在方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model
        //因此在thymeleaf中可以直接访问page对象中的数据

        //展示的总行数
        //传入的id设为0时,我们不以id进行查询,相当于查所有
        page.setRows(discussPostService.findDiscussPostRows(0));
        //配置当前路径
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getoffset(), page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        System.out.println("进入控制器");
        if(list != null){
            for(DiscussPost post:list){
                Map<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                System.out.println(user);
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        //调用模板thymeleaf中的方法
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }

}
