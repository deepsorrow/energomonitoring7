package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class OrganizationInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    public String organizationName;
    public String representativeName;
    public String chiefName;
    public String phoneNumber;
    public String address;
    @Transient
    public List<String> servingObjects;

    public String servingObjectString;

    public OrganizationInfo() {
    }

    public OrganizationInfo(int id, String organizationName, String representativeName, String phoneNumber,
                            String address, String servingObjectString, String chiefName) {
        this.id = id;
        this.organizationName = organizationName;
        this.representativeName = representativeName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.servingObjects = List.of(servingObjectString.split(";"));
        this.servingObjectString = servingObjectString;
        this.chiefName = chiefName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getServingObjects() {
        return servingObjects;
    }

    public void setServingObjects(List<String> servingObjects) {
        this.servingObjects = servingObjects;
    }
}
