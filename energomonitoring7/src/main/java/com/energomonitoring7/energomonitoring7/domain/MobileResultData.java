package com.energomonitoring7.energomonitoring7.domain;

import java.util.List;

public class MobileResultData {
    public String inspectorName;
    public String day;
    public String month;
    public String year;
    public String companyName;
    public String agreementNum;
    public String address;
    public List<DeviceTemperatureCounter> deviceTemperatureCounters;
    public List<DeviceFlowTransducer> deviceFlowTransducers;
    public List<DeviceTemperatureTransducer> deviceTemperatureTransducers;
    public List<DevicePressureTransducer> devicePressureTransducers;
    public List<DeviceCounter> deviceCounters;
}
