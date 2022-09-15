package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;

@Entity
public class DeviceTemperatureTransducer {

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
    public Field length = new Field("");
    public int typeId;

    public int dataId;

    @Convert(converter = FieldConverter.class)
    public Field lastCheckDate = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field installationPlace = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field values = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field comment = new Field("");

    public DeviceTemperatureTransducer() {
    }

    public DeviceTemperatureTransducer(String deviceName, String deviceNumber, String length, int typeId) {
        this.deviceName.initialValue   = deviceName;
        this.deviceNumber.initialValue = deviceNumber;
        this.length.initialValue = length;
        this.typeId = typeId;
    }

    public void resetId(){
        id = 0;
    }
}
