package com.airline.core.service;

import com.airline.core.model.Destination;
import com.airline.core.dto.DestinationRequest;
import com.airline.core.entity.DestinationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DestinationMapper {

    @Mapping(target = "latitude", source = "destinationRequest.location.latitude")
    @Mapping(target = "longitude", source = "destinationRequest.location.longitude")
    @Mapping(target = "airlines", expression = "java(new java.util.HashSet<>())")
    DestinationEntity map(DestinationRequest destinationRequest);

    @Mapping(target = "location.latitude", source = "destinationEntity.latitude")
    @Mapping(target = "location.longitude", source = "destinationEntity.longitude")
    Destination map(DestinationEntity destinationEntity);

    List<Destination> map(List<DestinationEntity> destinationEntities);
}
