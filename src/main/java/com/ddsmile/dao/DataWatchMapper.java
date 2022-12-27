package com.ddsmile.dao;

import com.ddsmile.entity.DataWatch;

/**
 *  节点数据表的数据访问层
 */
public interface DataWatchMapper {

    /**
     * 增加节点数据
     * @param dataWatch
     * @return
     */
    int insertDataWatch(DataWatch dataWatch);

    /**
     * 根据id查询节点数据
     * @param deviceId id
     * @return
     */
    DataWatch selectDataById(int deviceId);

    /**
     * 删除节点数据
     * @param deviceId id
     * @return
     */
    int deleteDataWatch(int deviceId);

}
