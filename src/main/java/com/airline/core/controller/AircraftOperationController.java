package com.airline.core.controller;

import com.airline.core.dto.BuyerAirlineRequest;
import com.airline.core.dto.RestErrorResponse;
import com.airline.core.service.AirlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Sell an existing aircraft for the airline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The aircraft was sold successfully"),
            @ApiResponse(responseCode = "404", description = "Aircraft was not found for the id provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))})})
    @PostMapping("/{aircraftId}/sell")
    public void sellAircraft(@PathVariable Long aircraftId) {
        log.info("Initiate sell the aircraft with id :: {} for the airline", aircraftId);
        airlineService.sellAircraft(aircraftId);
        log.info("Initiate sell the aircraft was successful for aircraft id :: {} for the airline", aircraftId);
    }

    @Operation(summary = "Buy an existing aircraft from another airline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The aircraft was bought successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Aircraft or buyer airline was not found for the id provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))})})
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
