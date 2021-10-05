package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class ResultData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Transient
    public OrganizationInfo organizationInfo;
    @Transient
    public ClientInfo clientInfo;
    @OneToOne(cascade = CascadeType.ALL)
    public OtherInfo otherInfo;
    @Transient
    public ProjectDescription project;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DeviceTemperatureCounter> deviceTemperatureCounters;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DeviceFlowTransducer> deviceFlowTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    public List<FlowTransducerCheckLengthResult> flowTransducerCheckLengthResults;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DeviceTemperatureTransducer> deviceTemperatureTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DevicePressureTransducer> devicePressureTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DeviceCounter> deviceCounters;

    public ResultData() {
    }

}
