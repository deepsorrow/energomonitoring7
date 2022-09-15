package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;

@Entity
public class DevicePressureTransducer {
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
    public Field sensorRange = new Field("");
    public int typeId;

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

    public DevicePressureTransducer() {
    }

    public DevicePressureTransducer(String deviceName, String deviceNumber, String sensorRange, int typeId) {
        this.deviceName.initialValue = deviceName;
        this.deviceNumber.initialValue = deviceNumber;
        this.sensorRange.initialValue = sensorRange;
        this.typeId = typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void resetId(){
        id = 0;
    }
}
