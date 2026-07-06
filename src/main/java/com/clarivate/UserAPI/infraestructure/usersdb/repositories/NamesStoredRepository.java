package com.clarivate.UserAPI.infraestructure.usersdb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clarivate.UserAPI.infraestructure.usersdb.model.NamesStoredEntity;

@Repository
public interface NamesStoredRepository extends JpaRepository<NamesStoredEntity, Long> {

}
