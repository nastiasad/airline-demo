package com.airline.core.controller;

import com.airline.core.dto.*;
import com.airline.core.model.Airline;
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
        log.info("Initiate the create airline, airlineRequest :: {}", airlineRequest);

        Airline airline = airlineService.createAirline(airlineRequest);
        return airlineDtoMapper.map(airline);
    }

    @GetMapping
    public List<AirlineResponse> getAirlines() {
        log.info("Initiate the get airlines");

        List<Airline> airlines = airlineService.getAirlines();
        return airlineDtoMapper.map(airlines);
    }

}
