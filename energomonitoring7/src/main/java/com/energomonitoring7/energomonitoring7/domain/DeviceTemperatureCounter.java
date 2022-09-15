package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;

@Entity
public class DeviceTemperatureCounter{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Version
    private Integer version;

    @Convert(converter = FieldConverter.class)
    public Field deviceName = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field deviceNumber = new Field("");

    public int typeId;
    public int dataId;

    @Convert(converter = FieldConverter.class)
    public Field unitSystem = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field modification = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field interval = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field lastCheckDate = new Field("");

    @Convert(converter = FieldConverter.class)
    public Field comment = new Field("");

    public DeviceTemperatureCounter() {
    }

    public DeviceTemperatureCounter(String deviceName, String deviceNumber, int typeId) {
        this.deviceName.initialValue = deviceName;
        this.deviceNumber.initialValue = deviceNumber;
        this.typeId = typeId;
    }

    public void resetId(){
        id = 0;
    }
}
