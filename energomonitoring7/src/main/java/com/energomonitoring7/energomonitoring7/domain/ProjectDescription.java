package com.energomonitoring7.energomonitoring7.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProjectDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(columnDefinition="text")
    private String photoBase64;

    @Column(columnDefinition="text")
    private String comment;
    public boolean isOk;

    public ProjectDescription() {
    }

    public ProjectDescription(String photoBase64, String comment, boolean isOk) {
        this.photoBase64 = photoBase64;
        this.comment = comment;
        this.isOk = isOk;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
