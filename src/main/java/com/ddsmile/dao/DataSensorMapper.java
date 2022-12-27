package com.ddsmile.dao;

import com.ddsmile.entity.DataSensor;

/**
 *  节点数据表(三合一)的数据访问层
 */
public interface DataSensorMapper {

    /**
     * 增加数据节点(三合一)
     * @param dataSensor
     * @return
     */
    int insertDataSensor(DataSensor dataSensor);

    /**
     * 查询最新一条数据
     * @return
     */
    DataSensor selectDataByTime();

    /**
     * 删除前100条数据
     * @return
     */
    int deleteSensor();
}
