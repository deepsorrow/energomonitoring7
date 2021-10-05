package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    private String agreementNumber;
    private String name;
    private String addressUUTE;
    private String phoneNumber;
    private String email;
    private String representativeName;
    private boolean hasDebt;

    @Transient
    public int dataId;

    public ClientInfo() {
    }

    public ClientInfo(String agreementNumber, String name, String addressUUTE, String representativeName,
                      String phoneNumber, String email, boolean hasDebt) {
        this.agreementNumber = agreementNumber;
        this.name = name;
        this.addressUUTE = addressUUTE;
        this.representativeName = representativeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hasDebt = hasDebt;
    }

    public boolean isHasDebt() {
        return hasDebt;
    }

    public void setHasDebt(boolean hasDebt) {
        this.hasDebt = hasDebt;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressUUTE() {
        return addressUUTE;
    }

    public void setAddressUUTE(String addressUUTE) {
        this.addressUUTE = addressUUTE;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }
}
