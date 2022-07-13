package com.airline.core.controller;

import com.airline.core.dto.AirlineResponse;
import com.airline.core.model.Airline;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AirlineDtoMapper {

    AirlineResponse map(Airline airline);

    List<AirlineResponse> map(List<Airline> airlines);

}
