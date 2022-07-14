package com.airline.core.controller;

import com.airline.core.dto.BuyerAirlineRequest;
import com.airline.core.service.AirlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/aircrafts")
@RequiredArgsConstructor
@Validated
public class AircraftOperationController {

    private final AirlineService airlineService;

    @PostMapping("/{aircraftId}/sell")
    public void sellAircraft(@PathVariable Long aircraftId) {
        log.info("Initiate sell the aircraft with id :: {} for the airline", aircraftId);
        airlineService.sellAircraft(aircraftId);
        log.info("Initiate sell the aircraft was successful for aircraft id :: {} for the airline", aircraftId);
    }

    @PostMapping("/{aircraftId}/buy")
    public void buyAircraft(@PathVariable Long aircraftId,
                            @Valid @RequestBody BuyerAirlineRequest buyerAirlineRequest) {
        log.info("Initiate buy the aircraft with id :: {} from another airline for airline, buyer airline request :: {}",
                aircraftId, buyerAirlineRequest);
        airlineService.buyAircraft(aircraftId, buyerAirlineRequest);
        log.info("Initiate buy the aircraft was successful for aircraft with id :: {}, buyer airline request :: {}",
                aircraftId, buyerAirlineRequest);
    }
}
