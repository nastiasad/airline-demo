package com.airline.core.controller;

import com.airline.core.dto.*;
import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;
import com.airline.core.service.AirlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Create a new airline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The airline was created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AirlineResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineResponse createAirline(@Valid @RequestBody AirlineRequest airlineRequest) {
        log.info("Initiate create airline, airlineRequest :: {}", airlineRequest);

        Airline airline = airlineService.createAirline(airlineRequest);
        log.info("Initiate create airline was successful, airlineRequest :: {}, airline created :: {}",
                airlineRequest, airline);
        return airlineDtoMapper.map(airline);
    }

    @Operation(summary = "Get a list of airlines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of airlines retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AirlineResponse.class)))})})
    @GetMapping
    public List<AirlineResponse> getAirlines() {
        log.info("Initiate get airlines");

        List<Airline> airlines = airlineService.getAirlines();
        log.info("Initiate get airlines was successful, {} airlines were found}", airlines.size());
        return airlineDtoMapper.map(airlines);
    }

    @Operation(summary = "Create aircraft")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The aircraft was created successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AircraftResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Airline was not found for the id provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))})})
    @PostMapping("/{airlineId}/aircrafts")
    @ResponseStatus(HttpStatus.CREATED)
    public AircraftResponse createAircraft(@PathVariable Long airlineId,
                                           @Valid @RequestBody AircraftRequest aircraftRequest) {
        log.info("Initiate add an aircraft for the airline with id :: {}, aircraftRequest :: {}",
                airlineId, aircraftRequest);

        Aircraft aircraft = airlineService.createAircraft(airlineId, aircraftRequest);
        log.info("Initiate add aircraft was successful, airline id :: {}, aircraftRequest :: {}, aircraft created :: {}",
                airlineId, aircraftRequest, aircraft);
        return airlineDtoMapper.map(aircraft);
    }

    @Operation(summary = "Create aircraft")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The destination was created successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DestinationResponse.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Airline was not found for the id provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))})})
    @PostMapping("/{airlineId}/destinations")
    @ResponseStatus(HttpStatus.CREATED)
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
