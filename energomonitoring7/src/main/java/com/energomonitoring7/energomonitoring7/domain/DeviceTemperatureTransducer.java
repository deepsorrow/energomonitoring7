package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeviceTemperatureTransducer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String deviceName;
    private String deviceNumber;
    private String length;
    private int typeId;

    public int dataId;
    public String lastCheckDate;
    public String installationPlace;
    public String values;
    public String comment;

    public DeviceTemperatureTransducer() {
    }

    public DeviceTemperatureTransducer(String deviceName, String deviceNumber, String length, int typeId) {
        this.deviceName   = deviceName;
        this.deviceNumber = deviceNumber;
        this.length = length;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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
