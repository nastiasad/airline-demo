package com.airline.core.controller;

import com.airline.core.dto.*;
import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;
import com.airline.core.service.AirlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/airlines")
@RequiredArgsConstructor
@Validated
public class AirlineController {

    private final AirlineService airlineService;
    private final AirlineDtoMapper airlineDtoMapper;

    @PostMapping
    public AirlineResponse createAirline(@Valid @RequestBody AirlineRequest airlineRequest) {
        log.info("Initiate create airline, airlineRequest :: {}", airlineRequest);

        Airline airline = airlineService.createAirline(airlineRequest);
        log.info("Initiate create airline was successful, airlineRequest :: {}, airline created :: {}",
                airlineRequest, airline);
        return airlineDtoMapper.map(airline);
    }

    @GetMapping
    public List<AirlineResponse> getAirlines() {
        log.info("Initiate get airlines");

        List<Airline> airlines = airlineService.getAirlines();
        log.info("Initiate get airlines was successful, {} airlines were found}", airlines.size());
        return airlineDtoMapper.map(airlines);
    }

    @PostMapping("/{airlineId}/aircrafts")
    public AircraftResponse createAircraft(@PathVariable Long airlineId,
                                           @Valid @RequestBody AircraftRequest aircraftRequest) {
        log.info("Initiate add an aircraft for the airline with id :: {}, aircraftRequest :: {}",
                airlineId, aircraftRequest);

        Aircraft aircraft = airlineService.createAircraft(airlineId, aircraftRequest);
        log.info("Initiate add aircraft was successful, airline id :: {}, aircraftRequest :: {}, aircraft created :: {}",
                airlineId, aircraftRequest, aircraft);
        return airlineDtoMapper.map(aircraft);
    }

    @PostMapping("/{airlineId}/destinations")
    public DestinationResponse addDestination(@PathVariable Long airlineId,
                                              @Valid @RequestBody DestinationRequest destinationRequest) {
        log.info("Initiate add a destination for the airline with id :: {}, destinationRequest :: {}",
                airlineId, destinationRequest);

        Destination destination = airlineService.createDestination(airlineId, destinationRequest);
        log.info("Initiate add a destination was successful for the airline with id :: {}, " +
                        "destinationRequest :: {}, destination created :: {}",
                airlineId, destinationRequest, destination);
        return airlineDtoMapper.map(destination);
    }

}
