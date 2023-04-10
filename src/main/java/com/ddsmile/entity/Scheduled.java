package com.ddsmile.entity;

/**
 * 定时任务实体类
 */
public class Scheduled {
    private String cronId;
    private String cronName;
    private String cron;

    public Scheduled() {
    }

    public Scheduled(String cronId, String cronName, String cron) {
        this.cronId = cronId;
        this.cronName = cronName;
        this.cron = cron;
    }

    @Override
    public String toString() {
        return "Scheduled{" +
                "cronId='" + cronId + '\'' +
                ", cronName='" + cronName + '\'' +
                ", cron='" + cron + '\'' +
                '}';
    }

    public String getCronId() {
        return cronId;
    }

    public void setCronId(String cronId) {
        this.cronId = cronId;
    }

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
