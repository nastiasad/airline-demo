package com.airline.core.repository;

import com.airline.core.entity.AirlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<AirlineEntity, Long> {

    Optional<AirlineEntity> findByName(String name);
}
