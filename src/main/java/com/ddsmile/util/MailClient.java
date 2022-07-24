package com.ddsmile.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮箱客户端，用于提供发邮件的功能
 * 需要的信息包含：
 * 1.发邮件的人（为固定）
 * 2.收邮件的人； 3. 邮件标题； 4. 邮件内容。
 */
@Component
public class MailClient {

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;

    //设置发件人，从配置文件中读取  mailSender
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送邮件
     * @param to 收邮件的人
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    public void sendMail(String to, String subject, String content){
        try {
            //创建模板
            MimeMessage message = mailSender.createMimeMessage();
            //使用帮助类构建内容
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败：" + e.getMessage());
        }
    }
}
