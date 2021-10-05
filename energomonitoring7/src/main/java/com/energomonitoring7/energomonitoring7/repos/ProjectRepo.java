package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.ProjectDescription;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepo extends CrudRepository<ProjectDescription, Integer> {
}
