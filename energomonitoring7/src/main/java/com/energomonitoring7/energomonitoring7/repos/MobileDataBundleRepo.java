package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.MobileDataBundle;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface MobileDataBundleRepo extends CrudRepository<MobileDataBundle, Integer> {

    //@Query("select mdb.id from MobileDataBundle mdb where mdb.clientInfo.id = ?1")
    //MobileDataBundle getByClientInfo_Id(int ClientInfoId);
    //ArrayList<MobileDataBundle> getByCompleted(int companyId, boolean completed);
}
