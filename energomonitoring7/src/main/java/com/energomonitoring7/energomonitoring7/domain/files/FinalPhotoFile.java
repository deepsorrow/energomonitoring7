package com.energomonitoring7.energomonitoring7.domain.files;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FinalPhotoFile extends BaseFile {
    @Id
    int id = 0;
    FinalPhotoType type = FinalPhotoType.Other;
}

