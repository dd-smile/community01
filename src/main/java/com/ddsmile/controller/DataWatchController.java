package com.ddsmile.controller;

import com.ddsmile.dao.DataSensorMapper;
import com.ddsmile.entity.DataSensor;
import com.ddsmile.mqtt.Callback;
import com.ddsmile.service.DataSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * 温湿度数据与CO2数据视图层
 */
@Controller
public class DataWatchController{

    @Autowired
    private DataSensorService dataSensorService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String addData(Model model){

        Callback callback = new Callback();
        System.out.println("进入节点数据页面");

        DataSensor dataSensor = dataSensorService.selectDataByT();
        model.addAttribute("temp", dataSensor.getSensorTemp());
        model.addAttribute("hum", dataSensor.getSensorHum());
        model.addAttribute("co2", dataSensor.getSensorCo2());


        //指定视图(模板引用使用的页面(html))
        return "/site/search";
    }

}
