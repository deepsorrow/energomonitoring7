package com.energomonitoring7.energomonitoring7.domain.files;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CheckLengthPhotoFile extends BaseFile {
    @Id
    public int id = 0;
    public int checkLengthParentId = 0;
}
