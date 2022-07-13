package com.airline.core.service;

import com.airline.core.dto.*;
import com.airline.core.model.Airline;

import java.util.List;

public interface AirlineService {

    Airline createAirline(AirlineRequest airlineRequest);

    Airline getAirline(Long airlineId);

    List<Airline> getAirlines();

}
