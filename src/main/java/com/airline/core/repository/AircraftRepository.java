package com.airline.core.repository;

import com.airline.core.entity.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {

    List<AircraftEntity> findByAirlineId(Long airlineId);
}
