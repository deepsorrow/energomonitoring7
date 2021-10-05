package com.energomonitoring7.energomonitoring7.domain.arshin;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ModelDetailed {

    @SerializedName("general")
    @Expose
    private General general;
    @SerializedName("manufacturer")
    @Expose
    private List<Manufacturer> manufacturer = null;
    @SerializedName("mit")
    @Expose
    private Mit mit;
    @SerializedName("status")
    @Expose
    private String status;

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public List<Manufacturer> getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(List<Manufacturer> manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Mit getMit() {
        return mit;
    }

    public void setMit(Mit mit) {
        this.mit = mit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}