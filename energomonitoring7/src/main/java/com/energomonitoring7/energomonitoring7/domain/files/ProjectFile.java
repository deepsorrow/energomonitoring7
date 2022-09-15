package com.energomonitoring7.energomonitoring7.domain.files;

import javax.persistence.*;

@Entity
public class ProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    @Column(columnDefinition="text")
    public String dataBase64;
    public String extension;
    public String title;

    public ProjectFile(String dataBase64, String extension, String title) {
        this.dataBase64 = dataBase64;
        this.extension = extension;
        this.title = title;
    }

    public ProjectFile() {

    }
}
