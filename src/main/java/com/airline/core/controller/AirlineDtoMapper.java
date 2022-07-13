package com.airline.core.controller;

import com.airline.core.dto.AircraftResponse;
import com.airline.core.dto.AirlineResponse;
import com.airline.core.dto.DestinationResponse;
import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AirlineDtoMapper {

    AirlineResponse map(Airline airline);

    List<AirlineResponse> map(List<Airline> airlines);

    AircraftResponse map(Aircraft aircraft);

    DestinationResponse map(Destination destination);
}
