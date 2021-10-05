package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.OrganizationInfo;
import org.springframework.data.repository.CrudRepository;

public interface ServingOrganizationsRepo extends CrudRepository<OrganizationInfo, Integer> {
    OrganizationInfo findByOrganizationName(String name);
}
