package com.energomonitoring7.energomonitoring7.domain;

import com.energomonitoring7.energomonitoring7.domain.utils.TemperatureCounterCharacteristicsParameter;
import org.junit.Ignore;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
public class DeviceCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Version
    private Integer version;

    private String deviceName;
    private String deviceNumber;
    private String impulseWeight;
    private String diameter;
    private int typeId;

    @Transient
    public ArrayList<TemperatureCounterCharacteristicsParameter> parameters;

    public DeviceCounter() {
    }

    public DeviceCounter(String deviceName, String deviceNumber, String impulseWeight, String diameter, int typeId) {
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

    public void resetId(){
        id = 0;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
