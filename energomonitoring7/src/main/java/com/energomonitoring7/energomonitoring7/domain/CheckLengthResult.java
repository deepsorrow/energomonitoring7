package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;

@Entity
public class CheckLengthResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Version
    private Integer version;

    public int dataId;

    public String lengthBefore = "";
    public String lengthAfter = "";

    @Column(columnDefinition="text")
    public String photosBase64;

    public int deviceOrder;
    public int icon;

    public CheckLengthResult() {
    }

    public void resetId(){
        id = 0;
    }
}
