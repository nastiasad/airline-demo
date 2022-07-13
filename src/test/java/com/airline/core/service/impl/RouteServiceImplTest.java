package com.airline.core.service.impl;

import com.airline.core.model.*;
import com.airline.core.service.AirlineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private AirlineService airlineService;

    @InjectMocks
    private RouteServiceImpl routeService;

    @Test
    void testGetRoutesSuccess() {
        // given
        long airlineId = 1L;
        boolean availableOnly = false;
        when(airlineService.getAirline(airlineId)).thenReturn(createAirline());
        when(airlineService.getDestinations(airlineId)).thenReturn(Arrays.asList(createDestinationMinsk(),
                createDestinationWarsaw(), createDestinationBerlin()));

        // when
        List<Route> routes = routeService.getRoutes(airlineId, availableOnly);

        // then
        assertThat(routes.size(), equalTo(3));

        Optional<Route> minskWarsawOpt = findRoute(routes, "Minsk", "Warsaw");
        assertThat(minskWarsawOpt.isPresent(), equalTo(true));
        assertThat(minskWarsawOpt.get().getHaversineDistance(), equalTo(474.58157219464005));

        Optional<Route> minskBerlinOpt = findRoute(routes, "Minsk", "Berlin");
        assertThat(minskBerlinOpt.isPresent(), equalTo(true));
        assertThat(minskBerlinOpt.get().getHaversineDistance(), equalTo(953.8104378072735));

        Optional<Route> warsawBerlinOpt = findRoute(routes, "Warsaw", "Berlin");
        assertThat(warsawBerlinOpt.isPresent(), equalTo(true));
        assertThat(warsawBerlinOpt.get().getHaversineDistance(), equalTo(517.4434428397087));
    }

    @Test
    void testGetRoutesEmpty() {
        // given
        long airlineId = 1L;
        boolean availableOnly = false;
        when(airlineService.getAirline(airlineId)).thenReturn(createAirline());
        when(airlineService.getDestinations(airlineId)).thenReturn(Collections.emptyList());

        // when
        List<Route> routes = routeService.getRoutes(airlineId, availableOnly);

        // then
        assertThat(routes.size(), equalTo(0));
    }

    @Test
    void testGetRoutesEmptyForOneDestination() {
        // given
        long airlineId = 1L;
        boolean availableOnly = false;
        when(airlineService.getAirline(airlineId)).thenReturn(createAirline());
        when(airlineService.getDestinations(airlineId)).thenReturn(Collections.singletonList(createDestinationMinsk()));

        // when
        List<Route> routes = routeService.getRoutes(airlineId, availableOnly);

        // then
        assertThat(routes.size(), equalTo(0));
    }

    @Test
    void testGetRoutesAvailableOnlySuccess() {
        // given
        long airlineId = 1L;
        boolean availableOnly = true;
        when(airlineService.getAirline(airlineId)).thenReturn(createAirline());
        when(airlineService.getDestinations(airlineId)).thenReturn(Arrays.asList(createDestinationMinsk(),
                createDestinationWarsaw(), createDestinationBerlin()));
        when(airlineService.getAircrafts(airlineId)).thenReturn(Arrays.asList(createAircraft(100.0),
                createAircraft(200.0), createAircraft(600.0)));

        // when
        List<Route> routes = routeService.getRoutes(airlineId, availableOnly);

        // then
        assertThat(routes.size(), equalTo(2));

        Optional<Route> minskBerlinOpt = findRoute(routes, "Minsk", "Berlin");
        assertThat(minskBerlinOpt.isPresent(), equalTo(false));

        Optional<Route> minskWarsawOpt = findRoute(routes, "Minsk", "Warsaw");
        assertThat(minskWarsawOpt.isPresent(), equalTo(true));
        assertThat(minskWarsawOpt.get().getHaversineDistance(), equalTo(474.58157219464005));

        Optional<Route> warsawBerlinOpt = findRoute(routes, "Warsaw", "Berlin");
        assertThat(warsawBerlinOpt.isPresent(), equalTo(true));
        assertThat(warsawBerlinOpt.get().getHaversineDistance(), equalTo(517.4434428397087));
    }

    private Optional<Route> findRoute(List<Route> routes, String fromName, String toName) {
        return routes.stream().filter(route -> fromName.equals(route.getFrom().getName())
                && toName.equals(route.getTo().getName())).findFirst();
    }

    private Airline createAirline() {
        return Airline.builder()
                .id(1L)
                .name("Belavia")
                .currentBalance(BigDecimal.TEN)
                .baseLocation(createDestinationMinsk())
                .build();
    }

    private Destination createDestinationMinsk() {
        return Destination.builder()
                .id(12L)
                .name("Minsk")
                .location(Location.builder()
                        .latitude(53.893009)
                        .longitude(27.567444)
                        .build())
                .build();
    }

    private Destination createDestinationWarsaw() {
        return Destination.builder()
                .id(2L)
                .name("Warsaw")
                .location(Location.builder()
                        .latitude(52.237049)
                        .longitude(21.017532)
                        .build())
                .build();
    }

    private Destination createDestinationBerlin() {
        return Destination.builder()
                .id(3L)
                .name("Berlin")
                .location(Location.builder()
                        .latitude(52.520008)
                        .longitude(13.404954)
                        .build())
                .build();
    }

    private Aircraft createAircraft(Double maxDistance) {
        return Aircraft.builder()
                .id(5L)
                .price(BigDecimal.valueOf(10000.30))
                .maxDistance(maxDistance)
                .build();
    }

}