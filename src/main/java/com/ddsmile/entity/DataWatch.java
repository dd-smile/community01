package com.ddsmile.entity;

import java.util.Date;
import java.util.Objects;

/**
 * 节点数据实体类
 */
public class DataWatch {

    private int deviceId;   //id
    private String sensorTag;  //传感器类型
    private String sensorName;  //传感器名字
    private float sensorVar;  //传感器数据
    private Date recordTime;  //记录时间

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getSensorTag() {
        return sensorTag;
    }

    public void setSensorTag(String sensorTag) {
        this.sensorTag = sensorTag;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public float getSensorVar() {
        return sensorVar;
    }

    public void setSensorVar(float sensorVar) {
        this.sensorVar = sensorVar;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "DataWatch{" +
                "deviceId=" + deviceId +
                ", sensorTag='" + sensorTag + '\'' +
                ", sensorName='" + sensorName + '\'' +
                ", sensorVar=" + sensorVar +
                ", recordTime=" + recordTime +
                '}';
    }

    public DataWatch() {
    }

    public DataWatch(int deviceId, String sensorTag, String sensorName, float sensorVar, Date recordTime) {
        this.deviceId = deviceId;
        this.sensorTag = sensorTag;
        this.sensorName = sensorName;
        this.sensorVar = sensorVar;
        this.recordTime = recordTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataWatch dataWatch = (DataWatch) o;
        return deviceId == dataWatch.deviceId &&
                Float.compare(dataWatch.sensorVar, sensorVar) == 0 &&
                Objects.equals(sensorTag, dataWatch.sensorTag) &&
                Objects.equals(sensorName, dataWatch.sensorName) &&
                Objects.equals(recordTime, dataWatch.recordTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, sensorTag, sensorName, sensorVar, recordTime);
    }
}
