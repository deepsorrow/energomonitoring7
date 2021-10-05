package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.ClientInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientInfoRepo extends CrudRepository<ClientInfo, Integer> {

    ClientInfo findByAgreementNumber(String agreementNumber);
    ClientInfo findById(int id);
    //List<ClientInfo> findAllById(List<Integer> ids);
}
