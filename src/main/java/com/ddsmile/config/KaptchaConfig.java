package com.ddsmile.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Kaptcha工具类配置文件
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer(){
        //起到配置文件的作用
        //以前都是从配置文件里读取值，这里直接现场写
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "100"); //图片宽度
        properties.setProperty("kaptcha.image.height", "40"); //图片高度
        properties.setProperty("kaptcha.textproducer.font.size", "32"); //字体大小
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0"); //黑色,RGB形式
        properties.setProperty("kaptcha.textproducer.char.String", "0123456789abcdefghigklmnopqrstuvwxyz"); //生成字符串的范围
        properties.setProperty("kaptcha.textproducer.char.length", "4"); //字符串的长度
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");  //噪声类型

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
