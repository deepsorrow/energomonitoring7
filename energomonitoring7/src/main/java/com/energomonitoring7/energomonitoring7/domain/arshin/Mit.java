package com.energomonitoring7.energomonitoring7.domain.arshin;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Mit {

    @SerializedName("part")
    @Expose
    private String part;
    @SerializedName("valid_for")
    @Expose
    private String validFor;
    @SerializedName("procedure")
    @Expose
    private String procedure;
    @SerializedName("interval")
    @Expose
    private String interval;
    @SerializedName("period")
    @Expose
    private String period;

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getValidFor() {
        return validFor;
    }

    public void setValidFor(String validFor) {
        this.validFor = validFor;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

}