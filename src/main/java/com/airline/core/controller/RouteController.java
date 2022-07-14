package com.airline.core.controller;

import com.airline.core.dto.RouteResponse;
import com.airline.core.model.Route;
import com.airline.core.service.RouteService;
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
