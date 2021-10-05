package com.energomonitoring7.energomonitoring7.repos;

import com.energomonitoring7.energomonitoring7.domain.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends CrudRepository<UserData, Integer> {
    UserData getByLoginAndPassword(String login, String password);

    @Query("SELECT u.name from UserData as u where u.login = :login")
    String getNameByLogin(@Param("login") String login);

    UserData getByLogin(@Param("login") String login);
}
