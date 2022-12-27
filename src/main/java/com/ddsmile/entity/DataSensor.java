package com.ddsmile.entity;

import java.util.Date;

/**
* 节点数据三合一实体类
 */
public class DataSensor {
    private int deviceId;   //id
    private String sensorTemp; //温度
    private String sensorHum; //湿度
    private String sensorCo2; //二氧化碳
    private Date recordTime;  //记录时间

    @Override
    public String toString() {
        return "DataSensor{" +
                "deviceId=" + deviceId +
                ", sensorTemp='" + sensorTemp + '\'' +
                ", sensorHum='" + sensorHum + '\'' +
                ", sensorCo2='" + sensorCo2 + '\'' +
                ", recordTime=" + recordTime +
                '}';
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getSensorTemp() {
        return sensorTemp;
    }

    public void setSensorTemp(String sensorTemp) {
        this.sensorTemp = sensorTemp;
    }

    public String getSensorHum() {
        return sensorHum;
    }

    public void setSensorHum(String sensorHum) {
        this.sensorHum = sensorHum;
    }

    public String getSensorCo2() {
        return sensorCo2;
    }

    public void setSensorCo2(String sensorCo2) {
        this.sensorCo2 = sensorCo2;
    }
}
