package com.ddsmile.service;

import com.ddsmile.dao.LoginTicketMapper;
import com.ddsmile.dao.UserMapper;
import com.ddsmile.entity.LoginTicket;
import com.ddsmile.entity.User;
import com.ddsmile.util.CommunityConstant;
import com.ddsmile.util.CommunityUtil;
import com.ddsmile.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 用户业务逻辑层
 * 用于把用户id转换成用户名
 */
@Service
public class UserService implements CommunityConstant {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    //以下值从配置文件中读取
    @Value("${community.path.domain}")
    //域名
    private String domain;

    @Value("${server.servlet.context-path}")
    //项目名
    private String contextPath;

    /**
     * 根据id查询一个用户
     * @param id
     * @return
     */
    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    // 由于注册是针对用户表的数据，因此注册逻辑写在UserService中

    /**
     * 注册的业务
     * @param user 新用户
     * @return
     */
    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        //空值处理
        if (user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }

        //验证账号：看是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg", "该账号已存在！");
            return map;
        }

        //验证邮箱: 看是否已经被注册过
//        u = userMapper.selectByEmail(user.getEmail());
//        if(u != null){
//            map.put("emailMsg", "该邮箱已被注册！");
//            return map;
//        }

        //注册
        //生成随机字符串，添加到密码上
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());

        //设置默认随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//添加到数据库

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //激活路径格式：http://localhost:8080/community/activation/101/code
        //注：用户ID为主键自动生成 ,注册传进来的User是没有id的, 但是当调用insertUser方法时, 会自动生成
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        //最后返回的map为空，则说明没有问题
        return map;
    }

    /**
     * 激活
     * 需要传进用户ID，得到该用户的激活码，与传进来的激活码进行对比
     * @param userId
     * @param code
     * @return
     */
    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        //如果可以查询到该用户状态为1，说明已经有了，重复激活
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)){
            //激活码正确，则将用户的状态设为1
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 登录的业务
     * @param username 用户名
     * @param password 密码
     * @param expiredSeconds 过期的秒数
     * @return
     */
    public Map<String,Object> login(String username, String password, int expiredSeconds){
        Map<String,Object> map = new HashMap<>();
        //空值的处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空!");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空!");
            return map;
        }

        //验证账号
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","该账号不存在！");
            return map;
        }

        //验证账号是否激活
        if(user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活！");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());//把明文密码加密后再与数据库中的密码进行对比
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确!");
            return map;
        }

        //到这就说明登陆成功了，现在生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    /**
     * 退出登录：将凭证改为1，即失效状态
     * @param ticket 用户登录凭证
     */
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    /**
     * 根据输入的登录凭证，返回loginTicket对象，里面存有userid
     * 运行逻辑为：浏览器发送请求后，cookie中携带登录凭证ticket
     * 服务器根据ticket查询出对应的用户id
     * @param ticket 凭证
     * @return
     */
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    /**
     * 更新用户头像
     * @param userId 用户id
     * @param headurl 头像路径
     * @return
     */
    public int updateHeader(int userId, String headurl){
        return userMapper.updateHeader(userId, headurl);
    }
}
