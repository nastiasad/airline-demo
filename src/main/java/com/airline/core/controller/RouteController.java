package com.airline.core.controller;

import com.airline.core.dto.RestErrorResponse;
import com.airline.core.dto.RouteResponse;
import com.airline.core.model.Route;
import com.airline.core.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/airlines/{airlineId}/destinations")
@RequiredArgsConstructor
@Validated
public class RouteController {

    private final RouteService routeService;
    private final RouteDtoMapper routeDtoMapper;

    @Operation(summary = "Get routes for the airline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The routes retrieved successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RouteResponse.class)))}),
            @ApiResponse(responseCode = "404", description = "Airline was not found for the id provided",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestErrorResponse.class))})})
    @GetMapping("/routes")
    public List<RouteResponse> getRoutes(@PathVariable Long airlineId,
                                         @RequestParam(required = false, defaultValue = "false") boolean availableOnly) {
        log.info("Initiate list distance from all the destinations for the airline with id :: {}, availableOnly :: {}",
                airlineId, availableOnly);
        List<Route> routes = routeService.getRoutes(airlineId, availableOnly);
        log.info("Initiate list distance was successful, airline id :: {}, availableOnly :: {}, routs found :: {}",
                airlineId, availableOnly, routes);
        return routeDtoMapper.map(routes);
    }
}
