package com.ddsmile.dao;

import com.ddsmile.entity.Scheduled;

/**
 * 定时任务数据访问层
 */
public interface ScheduledMapper {

    /**
     * 根据id查询定时任务时间点
     * @param cronId 定时器id
     * @return
     */
    Scheduled selectCronById(String cronId);

    /**
     * 更新定时任务时间点
     * @param cronId 定时器id
     * @param cron 定时任务时间点
     * @return
     */
    int updateCron(String cronId,String cron);
}
