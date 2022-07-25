package com.ddsmile.controller;

import com.ddsmile.entity.DiscussPost;
import com.ddsmile.entity.User;
import com.ddsmile.service.DiscussPostService;
import com.ddsmile.service.UserService;
import com.ddsmile.util.CommunityUtil;
import com.ddsmile.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 所有与发帖相关的请求
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired    //获取当前用户
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    //发布帖子
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null){
            // 403表示没有权限
            return CommunityUtil.getJSONString(403, "你还没有登录哦！");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        return CommunityUtil.getJSONString(0, "发布成功");
    }

    //查询帖子
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model){
        //查询得到帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        //将帖子传给模板
        model.addAttribute("post",post);
        //查询出发帖人
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        return "/site/discuss-detail";
    }


}
