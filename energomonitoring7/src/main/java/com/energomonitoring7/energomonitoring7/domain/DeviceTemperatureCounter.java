package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeviceTemperatureCounter{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String deviceName;
    private String deviceNumber;
    private int typeId;
    public int dataId;

    public String unitSystem;
    public String modification;
    public String interval;
    public String lastCheckDate;
    public String comment;

    public DeviceTemperatureCounter() {
    }

    public DeviceTemperatureCounter(String deviceName, String deviceNumber, int typeId) {
        this.deviceName = deviceName;
        this.deviceNumber = deviceNumber;
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

    public int getTypeId() {
        return typeId;
    }

    public void resetId(){
        id = 0;
    }
}
