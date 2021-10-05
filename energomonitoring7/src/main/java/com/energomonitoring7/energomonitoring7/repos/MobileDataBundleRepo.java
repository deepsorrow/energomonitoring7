package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.MobileDataBundle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface MobileDataBundleRepo extends CrudRepository<MobileDataBundle, Integer> {

    //@Query("select mdb.id from MobileDataBundle mdb where mdb.clientInfo.id = ?1")
    //MobileDataBundle getByClientInfo_Id(int ClientInfoId);
    ArrayList<MobileDataBundle> getByUserIdAndCompleted(int userId, boolean completed);
}
