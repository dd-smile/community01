package com.ddsmile.service;

import com.ddsmile.dao.ScheduledMapper;
import com.ddsmile.entity.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时器数据访问业务层
 */
@Service
public class ScheduledService {

    @Autowired
    private ScheduledMapper scheduledMapper;

    /**
     * 查询定时任务
     * @return
     */
    public Scheduled findCronById(String cronId) {
        return scheduledMapper.selectCronById(cronId);
    }

    /**
     * 更新定时任务时间点
     * @param cronId 定时器id
     * @param cron 定时任务时间点
     */
    public void updateCronBy(String cronId,String cron){
        scheduledMapper.updateCron(cronId, cron);
    }
}
