package com.energomonitoring7.energomonitoring7.domain;

import com.energomonitoring7.energomonitoring7.domain.files.FinalPhotoFile;

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
    public List<CheckLengthResult> checkLengthResults;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DeviceTemperatureTransducer> deviceTemperatureTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DevicePressureTransducer> devicePressureTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    public List<DeviceCounter> deviceCounters;
    @OneToMany(cascade = CascadeType.ALL)
    public List<FinalPhotoFile> finalPhotos;

    public ResultData() {
    }

}
