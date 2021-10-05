package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeviceFlowTransducer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String deviceName;
    private String deviceNumber;
    private String impulseWeight;
    private String diameter;
    private int typeId;

    public String lastCheckDate;
    public String installationPlace;
    public String manufacturer;
    public String values;
    public String comment;

    public DeviceFlowTransducer() {
    }

    public DeviceFlowTransducer(String deviceName, String deviceNumber, String impulseWeight, String diameter, int typeId) {
        this.deviceName = deviceName;
        this.deviceNumber = deviceNumber;
        this.impulseWeight = impulseWeight;
        this.diameter = diameter;
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

    public String getImpulseWeight() {
        return impulseWeight;
    }

    public void setImpulseWeight(String impulseWeight) {
        this.impulseWeight = impulseWeight;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public int getTypeId() {
        return typeId;
    }

    public void resetId(){
        id = 0;
    }
}
