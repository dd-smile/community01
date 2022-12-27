package com.ddsmile.service;

import com.ddsmile.dao.DataSensorMapper;
import com.ddsmile.entity.DataSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据访问业务层
 */
@Service
public class DataSensorService {
    @Autowired
    private DataSensorMapper dataSensorMapper;

    /**
     * 查询最新一条节点数据
     * @return
     */
    public DataSensor selectDataByT(){
        return dataSensorMapper.selectDataByTime();
    }

    /**
     * 增加节点数据
     * @param dataSensor
     * @return
     */
    public int addDataSensor(DataSensor dataSensor){
        return dataSensorMapper.insertDataSensor(dataSensor);
    }




}
