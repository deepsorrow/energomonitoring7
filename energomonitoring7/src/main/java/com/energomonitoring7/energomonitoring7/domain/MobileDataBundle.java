package com.energomonitoring7.energomonitoring7.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MobileDataBundle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public int organizationId;
    public int clientId;

    public int userId;
    //@OneToOne
    //@JoinColumn(name = "organizationId", referencedColumnName = "id", insertable = false, updatable = false)
    @Transient
    public OrganizationInfo organizationInfo;
    //@OneToOne
    //@JoinColumn(name = "clientId", referencedColumnName = "id", insertable = false, updatable = false)
    @Transient
    private ClientInfo clientInfo;
    @OneToOne(cascade = CascadeType.ALL)
    private ProjectDescription project;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DeviceTemperatureCounter> deviceTemperatureCounters;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DeviceFlowTransducer> deviceFlowTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DeviceTemperatureTransducer> deviceTemperatureTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DevicePressureTransducer> devicePressureTransducers;
    @OneToMany(cascade = CascadeType.ALL)
    private List<DeviceCounter> deviceCounters;
    public boolean completed;

    protected MobileDataBundle() {
    }

    public MobileDataBundle(int userId, ClientInfo clientInfo) {
        this.userId = userId;
        this.clientInfo = clientInfo;
        this.clientId   = clientInfo.id;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ProjectDescription getProject() {
        return project;
    }

    public void setProject(ProjectDescription project) {
        this.project = project;
    }

    public void setDevices(List<DeviceTemperatureCounter> deviceTemperatureCounters,
                           List<DeviceFlowTransducer> deviceFlowTransducers,
                           List<DeviceTemperatureTransducer> deviceTemperatureTransducers,
                           List<DevicePressureTransducer> devicePressureTransducers,
                           List<DeviceCounter> deviceCounters) {
        this.deviceTemperatureCounters = deviceTemperatureCounters;
        this.deviceFlowTransducers = deviceFlowTransducers;
        this.deviceTemperatureTransducers = deviceTemperatureTransducers;
        this.devicePressureTransducers = devicePressureTransducers;
        this.deviceCounters = deviceCounters;
    }

    public List<DeviceTemperatureCounter> getDeviceTemperatureCounters() {
        return deviceTemperatureCounters;
    }

    public List<DeviceFlowTransducer> getDeviceFlowTransducers() {
        return deviceFlowTransducers;
    }

    public List<DeviceTemperatureTransducer> getDeviceTemperatureTransducers() {
        return deviceTemperatureTransducers;
    }

    public List<DevicePressureTransducer> getDevicePressureTransducers() {
        return devicePressureTransducers;
    }

    public List<DeviceCounter> getDeviceCounters() {
        return deviceCounters;
    }
}
