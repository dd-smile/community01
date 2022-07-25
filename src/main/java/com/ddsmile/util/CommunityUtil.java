package com.ddsmile.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 为密码随机生成一个字符串后缀，用来加强密码；
 * 将密码进行MD5加密。
 */
public class CommunityUtil {
    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * MD5加密
     * hello + 3e4r8 --> abc13huoad34
     * @param key
     * @return
     */
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 得到JSON格式的字符串
     * @param code 编号
     * @param msg 提示信息
     * @param map 封装里的业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        //封装到json对象中
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if (map!=null){
            for (String key: map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    /**
     * 得到JSON格式的字符串（重载1：无业务数据）
     * @param code 编号
     * @param msg 提示信息
     * @return
     */
    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }

    /**
     * 得到JSON格式的字符串（重载2：无提示、业务数据）
     * @param code 编号
     * @return
     */
    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }

//测试
//    public static void main(String[] args) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("name", "张三");
//        map.put("age", "25");
//        System.out.println(getJSONString(0, "ok", map));
//    }

}
