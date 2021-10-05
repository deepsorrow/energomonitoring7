package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
        import java.util.ArrayList;

@Entity
public class FlowTransducerCheckLengthResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public int dataId;

    public String lengthBefore = "";
    public String lengthAfter = "";

    @Column(columnDefinition="text")
    public String photosBase64;

    public int deviceOrder;
    public int icon;

    public FlowTransducerCheckLengthResult() {
    }

    public void resetId(){
        id = 0;
    }
}
