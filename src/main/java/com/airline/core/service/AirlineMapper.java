package com.airline.core.service;

import com.airline.core.dto.AirlineRequest;
import com.airline.core.entity.AirlineEntity;
import com.airline.core.model.Airline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {DestinationMapper.class})
public interface AirlineMapper {

    @Mapping(target = "currentBalance", source = "balance")
    Airline map(AirlineEntity airlineEntity);

    @Mapping(target = "balance", source = "initialBudget")
    @Mapping(target = "destinations", expression = "java(new java.util.HashSet<>())")
    AirlineEntity map(AirlineRequest airlineRequest);

    List<Airline> map(List<AirlineEntity> airlineEntities);
}
