package com.orpheum.orchestrator.backstage.portal.repository;

import com.orpheum.orchestrator.backstage.portal.model.user.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserDataRepository extends CrudRepository<UserData, Long> {

    UserData findByEmail(String email);

}
