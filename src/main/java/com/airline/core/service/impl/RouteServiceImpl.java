package com.airline.core.service.impl;

import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;
import com.airline.core.model.Route;
import com.airline.core.service.AirlineService;
import com.airline.core.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.SloppyMath;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final AirlineService airlineService;

    @Override
    public List<Route> getRoutes(Long airlineId, boolean availableOnly) {
        Airline airline = airlineService.getAirline(airlineId);
        List<Destination> destinations = airlineService.getDestinations(airlineId);

        List<Route> routes = new ArrayList<>();
        int n = destinations.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Destination from = destinations.get(i);
                Destination to = destinations.get(j);
                double haversineDistance = SloppyMath.haversinKilometers(from.getLocation().getLatitude(),
                        from.getLocation().getLongitude(), to.getLocation().getLatitude(), to.getLocation().getLongitude());
                Route route = Route.builder()
                        .from(from)
                        .to(to)
                        .haversineDistance(haversineDistance)
                        .build();
                routes.add(route);
            }
        }

        if (availableOnly) {
            return filterAvailable(routes, airline);
        }
        return routes;
    }

    private List<Route> filterAvailable(List<Route> routes, Airline airline) {
        Double maxAircraftDistance = findMaximalMaxDistance(airline.getId());
        return routes.stream()
                .filter(route -> route.getHaversineDistance() < maxAircraftDistance)
                .collect(Collectors.toList());
    }

    private Double findMaximalMaxDistance(Long airlineId) {
        return airlineService.getAircrafts(airlineId)
                .stream()
                .max(Comparator.comparing(Aircraft::getMaxDistance))
                .map(Aircraft::getMaxDistance)
                .orElse(-1d);
    }
}
