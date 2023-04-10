package com.ddsmile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddsmile.dao.DataSensorMapper;
import com.ddsmile.entity.DataSensor;
import com.ddsmile.mqtt.Callback;
import com.ddsmile.mqtt.MQTTListener;
import com.ddsmile.service.DataSensorService;
import com.ddsmile.service.ScheduledService;
import com.ddsmile.util.ButtonUtil;
import com.ddsmile.util.MyTask;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.ddsmile.mqtt.MQTTConnect;


/**
 * 温湿度数据与CO2数据视图层
 */
@Controller
public class DataWatchController{

    @Autowired
    private DataSensorService dataSensorService;

    @Autowired
    private ScheduledService scheduledService;

//    Callback callback = new Callback();  //测试Map取得到数据吗?
//    Map<String,Object> map = new HashMap<>();

    ButtonUtil test = new ButtonUtil();
    JSONObject object = new JSONObject();

    @Autowired
    private MQTTListener mqttListener;  //用于建立发布消息连接


    /*
    显示数据
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String addData(Model model){

        System.out.println("进入节点数据页面");

        //查询一条最新的节点数据
        DataSensor dataSensor = dataSensorService.selectDataByT();

//        map = callback.getMap();
//        System.out.println(map.get("temp"));
//        System.out.println(map.get("hum"));
//        System.out.println(map.get("co2"));

        /**
         * 进行阀值判断
         * 二氧化碳正常值是 400 ~ 700ppm
         * 温度正常值是 0 ~ 37°
         * 湿度正常值是 35% ~ 75%
         */
        //判断温度
        if((Integer.parseInt(dataSensor.getSensorTemp()) < 0) || ((Integer.parseInt(dataSensor.getSensorTemp()) > 37) )){
            model.addAttribute("nt","温度不正常");
            model.addAttribute("temp", dataSensor.getSensorTemp());
        }else {
            model.addAttribute("nt","温度正常");
            model.addAttribute("temp", dataSensor.getSensorTemp());
        }
        //判断湿度
        if((Integer.parseInt(dataSensor.getSensorHum()) < 35) || ((Integer.parseInt(dataSensor.getSensorHum()) > 75) )){
            model.addAttribute("nh","湿度不正常");
            model.addAttribute("hum", dataSensor.getSensorHum());
        }else {
            model.addAttribute("nh","湿度正常");
            model.addAttribute("hum", dataSensor.getSensorHum());
        }
        //判断二氧化碳
        if((Integer.parseInt(dataSensor.getSensorCo2()) < 400) || ((Integer.parseInt(dataSensor.getSensorCo2()) > 500) )){
            model.addAttribute("nc","二氧化碳浓度不正常");
            model.addAttribute("co2", dataSensor.getSensorCo2());
        }else {
            model.addAttribute("nc","二氧化碳浓度正常");
            model.addAttribute("co2", dataSensor.getSensorCo2());
        }


        //指定视图(模板引用使用的页面(html))
        return "/site/search";
    }

    /*
    设置开关值与定时任务
     */
    @RequestMapping(value = "/subbutton", method = RequestMethod.POST)
    public String subButton(String sub,String task,Model model) throws MqttException {

//        MQTTConnect mqttConnect = new MQTTConnect();
//        mqttConnect.setMqttClient("admin", "7815csdd.", new Callback());
        /*
        判断开关值
         */
        if( !((sub).equals("开")) && !((sub).equals("关"))  ){
            model.addAttribute("msg","开关值错误!");
            System.out.println("sub" + sub);
        }else{
            if((sub).equals("开"))
            {
                test.flag = 1;
                test.buttonName = "LED_SW";
                object.put(test.buttonName,test.flag);
                String msg = JSON.toJSONString(object);
                mqttListener.getMqttConnect().pub("/mytopic/sub",msg,0);
            }else{
                test.flag = 0;
                test.buttonName = "LED_SW";
                object.put(test.buttonName,test.flag);
                String msg = JSON.toJSONString(object);
                mqttListener.getMqttConnect().pub("/mytopic/sub",msg,0);
            }
            System.out.println("sub = " + sub);
        }

        /**
         * 设置定时任务
         */
        if (task == null){
            //
        }else {
            switch (task)
            {
                case "每隔5秒钟执行一次":
                    scheduledService.updateCronBy("1", "0/5 * * * * ?");
                    break;
                case "每隔10秒钟执行一次":
                    scheduledService.updateCronBy("1", "0/10 * * * * ?");
                    break;
                case "每隔30秒钟执行一次":
                    scheduledService.updateCronBy("1", "0/30 * * * * ?");
                    break;
                case "每隔1分钟执行一次":
                    scheduledService.updateCronBy("1", "0 0/1 * * * ?");
                    break;
                case "每天23点55分执行一次":
                    scheduledService.updateCronBy("1", "0 55 23 * * ?");
                    break;
                case "每周六8点执行一次":
                    scheduledService.updateCronBy("1", "0 0 8 ? * L");
                    break;
                case "每月最后一个周五，每隔2小时执行一次":
                    scheduledService.updateCronBy("1", "0 0 */2 ? * 6L");
                    break;
                case "每月的第三个星期五上午10:15执行一次":
                    scheduledService.updateCronBy("1", "0 15 10 ? * 6L");
                    break;
                case "朝九晚五工作时间内每半小时执行一次":
                    scheduledService.updateCronBy("1", "0 0/30 9-17 * * ?");
                    break;
                case "每个星期三中午12点执行一次":
                    scheduledService.updateCronBy("1", "0 0 12 ? * 4");
                    break;
                case "每天上午10点，下午2点，4点执行一次":
                    scheduledService.updateCronBy("1", "0 0 10,14,16 * * ?");
                    break;
                case "每分钟的第10秒与第20秒都会执行":
                    scheduledService.updateCronBy("1", "10,20 * * * * ?");
                    break;
            }
        }

        return "/site/search";
    }


    //        表达式	                                意义
//        每隔5秒钟执行一次	                        0/5 * * * * ?
//        每隔10秒钟执行一次	                        0/10 * * * * ?
//        每隔30秒钟执行一次	                        0/30 * * * * ?
//        每隔1分钟执行一次 　	                    0 0/1 * * * ?
//        每天23点55分执行一次	                    0 55 23 * * ?
//        每周六8点执行一次	                        0 0 8 ? * L
//        每月最后一个周五，每隔2小时执行一次 　	        0 0 */2 ? * 6L
//        每月的第三个星期五上午10:15执行一次	        0 15 10 ? * 6L
//        朝九晚五工作时间内每半小时执行一次	            0 0/30 9-17 * * ?
//        每个星期三中午12点执行一次 　	            0 0 12 ? * 4
//        每天上午10点，下午2点，4点执行一次	        0 0 10,14,16 * * ?
//        每分钟的第10秒与第20秒都会执行               10,20 * * * * ?


    public ButtonUtil getTest() {
        return test;
    }
}
