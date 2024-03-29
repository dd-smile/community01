package com.ddsmile.controller;

import com.ddsmile.annotation.LoginRequired;
import com.ddsmile.entity.User;
import com.ddsmile.service.FollowService;
import com.ddsmile.service.LikeService;
import com.ddsmile.service.UserService;
import com.ddsmile.util.CommunityConstant;
import com.ddsmile.util.CommunityUtil;
import com.ddsmile.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 账号设置
 * 上传头像 -> 获取头像
 * 修改密码
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private  static final Logger logger = LoggerFactory.getLogger(UserController.class);

    //注入文件的上传路径
    @Value("${community.path.upload}")
    private String uploadPath;
    //注入域名
    @Value("${community.path.domain}")
    private String domain;
    //访问路径
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    //进入账号设置
    @LoginRequired
    @RequestMapping(path="/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    //上传头像
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        //如果没上传头像，就点击上传头像按钮
        if(headerImage == null){
            model.addAttribute("error","您还没有选择图片！");
            return "/site/setting";
        }
        //获取图片后缀名
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){    //如果后缀名为空
            model.addAttribute("error","文件格式不正确！");
            return "/site/setting";
        }
        //生成随机的文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }
        //到此就可以正常进行了
        //更新当前用户头像的路径（Web访问路径）
        //格式：http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    //获取头像（和上面头像设置时的路径格式保持一致）
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器存放路径
        fileName = uploadPath + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片（浏览器响应图片时，为此写法）
        response.setContentType("image/" + suffix);
        //获取字节流
        try (
                //此为java7写法，这里面声明的变量会自动加finally，在里面自动关闭
                //而输出流会被SpringMVC自动关闭
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ){
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = fis.read(buffer)) != -1){
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改用户密码
    @LoginRequired
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(String originalPassword,String newPassword,String confirmPassword,Model model) {
        if (originalPassword == null) {
            model.addAttribute("originalPasswordMsg", "请输入原始密码!");
            return "/site/setting";
        }
        if (newPassword == null) {
            model.addAttribute("newPasswordMsg", "请输入新密码!");
            return "/site/setting";
        }
        if (confirmPassword == null) {
            model.addAttribute("confirmPasswordMsg", "请确认密码!");
            return "/site/setting";
        }

        //确认账号
        User user = hostHolder.getUser();
        if (!CommunityUtil.md5(originalPassword + user.getSalt()).equals(user.getPassword())) {
            model.addAttribute("originalPasswordMsg", "密码错误!");
            return "/site/setting";
        }
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("confirmPasswordMsg", "两次输入的密码不一致!");
            return "/site/setting";
        }
        userService.updatePassword(user.getId(), CommunityUtil.md5(newPassword + user.getSalt()));
        return "redirect:/login";
    }

    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

}
