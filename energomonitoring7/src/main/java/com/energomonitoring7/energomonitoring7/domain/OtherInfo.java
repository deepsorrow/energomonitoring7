package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class OtherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    public int dataId;
    public int clientId;
    public int organizationId;
    public int projectId;
    public int userId;
    public Date date;

    @Version
    private Integer version;

    public boolean lightIsOk;
    public boolean sanPinIsOk;
    @Column(columnDefinition="text")
    public String generalInspectionComment;
    @Column(columnDefinition="text")
    public String counterCharacteristicts;
    @Column(columnDefinition="text")
    public String counterCharacteristictsComment;
    @Column(columnDefinition="text")
    public String finalPhotosGeneral;
    @Column(columnDefinition="text")
    public String finalPhotosDevices;
    @Column(columnDefinition="text")
    public String finalPhotosSeals;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OtherInfo() {
    }

    public OtherInfo(int dataId, boolean lightIsOk, boolean sanPinIsOk, String generalInspectionComment) {
        this.dataId = dataId;
        this.lightIsOk = lightIsOk;
        this.sanPinIsOk = sanPinIsOk;
        this.generalInspectionComment = generalInspectionComment;
    }
}
