package com.energomonitoring7.energomonitoring7.domain;

import com.energomonitoring7.energomonitoring7.domain.files.ProjectFile;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProjectDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @OneToMany(cascade = CascadeType.ALL)
    public List<ProjectFile> files;

    @Column(columnDefinition="text")
    private String comment;
    public boolean isOk;

    public ProjectDescription() {
    }

    public ProjectDescription(List<ProjectFile> files, String comment, boolean isOk) {
        this.files = files;
        this.comment = comment;
        this.isOk = isOk;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
