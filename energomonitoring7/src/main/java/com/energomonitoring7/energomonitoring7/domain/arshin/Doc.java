
package com.energomonitoring7.energomonitoring7.domain.arshin;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Doc {

    @SerializedName("mi.mitnumber")
    private String miMitnumber;
    @SerializedName("valid_date")
    private String validDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getMiMitnumber() {
        return miMitnumber;
    }

    public void setMiMitnumber(String miMitnumber) {
        this.miMitnumber = miMitnumber;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
