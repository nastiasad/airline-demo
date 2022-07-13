package com.airline.core.repository;

import com.airline.core.entity.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {

    Optional<DestinationEntity> findByLatitudeAndLongitude(Double latitude, Double longitude);

    @Query(value = "SELECT d FROM DestinationEntity d INNER JOIN FETCH d.airlines a WHERE a.id = :airlineId")
    List<DestinationEntity> findByAirlineId(Long airlineId);
}
