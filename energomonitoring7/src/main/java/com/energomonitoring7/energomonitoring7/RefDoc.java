package com.energomonitoring7.energomonitoring7;

import javax.persistence.*;

@Entity
public class RefDoc {
    @Id
    public int id;
    public int parentId;
    public boolean isFolder;
    public String parentFolderName;
    public String title;
    public String path;
    public String size;
    public String localFilePath;
    public String dataString;

    public RefDoc() {

    }

    public RefDoc(int id, int parentId, boolean isFolder, String path, String parentFolder, String title, String size) {
        this.id = id;
        this.parentId = parentId;
        this.isFolder = isFolder;
        this.path = path;
        this.parentFolderName = parentFolder;
        this.title = title;
        this.size = size;
    }
}
