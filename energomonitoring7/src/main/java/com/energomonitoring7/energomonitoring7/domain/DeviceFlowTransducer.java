package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;

@Entity
public class DeviceFlowTransducer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Version
    private Integer version;

    @Convert(converter = FieldConverter.class)
    public Field deviceName = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field deviceNumber = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field impulseWeight = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field diameter = new Field("");

    private int typeId;

    @Convert(converter = FieldConverter.class)
    public Field lastCheckDate = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field installationPlace = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field manufacturer = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field values = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field comment = new Field("");

    public DeviceFlowTransducer() {
    }

    public DeviceFlowTransducer(String deviceName, String deviceNumber, String impulseWeight, String diameter, int typeId) {
        this.deviceName.initialValue = deviceName;
        this.deviceNumber.initialValue = deviceNumber;
        this.impulseWeight.initialValue = impulseWeight;
        this.diameter.initialValue = diameter;
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void resetId(){
        id = 0;
    }
}
