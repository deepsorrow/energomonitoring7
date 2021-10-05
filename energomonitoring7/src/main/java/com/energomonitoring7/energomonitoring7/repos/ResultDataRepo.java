package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.ResultData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResultDataRepo extends CrudRepository<ResultData, Integer> {
    List<ResultData> findByOtherInfo_UserId(int userId);
}
