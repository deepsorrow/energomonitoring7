package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DevicePressureTransducer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String deviceName;
    private String deviceNumber;
    private String sensorRange;
    private int typeId;

    public String lastCheckDate;
    public String installationPlace;
    public String manufacturer;
    public String values;
    public String comment;

    public DevicePressureTransducer() {
    }

    public DevicePressureTransducer(String deviceName, String deviceNumber, String sensorRange, int typeId) {
        this.deviceName = deviceName;
        this.deviceNumber = deviceNumber;
        this.sensorRange = sensorRange;
        this.typeId = typeId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getSensorRange() {
        return sensorRange;
    }

    public void setSensorRange(String sensorRange) {
        this.sensorRange = sensorRange;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void resetId(){
        id = 0;
    }
}
