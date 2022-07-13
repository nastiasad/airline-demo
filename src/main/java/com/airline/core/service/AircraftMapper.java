package com.airline.core.service;

import com.airline.core.dto.AircraftRequest;
import com.airline.core.entity.AircraftEntity;
import com.airline.core.model.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AircraftMapper {

    AircraftEntity map(AircraftRequest aircraftRequest);

    @Mapping(target = "airlineId", source = "aircraft.airline.id")
    Aircraft map(AircraftEntity aircraft);

    List<Aircraft> map(List<AircraftEntity> aircraftEntities);
}
