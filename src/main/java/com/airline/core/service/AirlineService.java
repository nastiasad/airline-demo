package com.airline.core.service;

import com.airline.core.dto.*;
import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;

import java.util.List;

public interface AirlineService {

    Airline createAirline(AirlineRequest airlineRequest);

    Airline getAirline(Long airlineId);

    List<Airline> getAirlines();

    Aircraft createAircraft(Long airlineId, AircraftRequest aircraftRequest);

    List<Aircraft> getAircrafts(Long airlineId);

    Destination createDestination(Long airlineId, DestinationRequest destinationRequest);

    List<Destination> getDestinations(Long airlineId);
}
