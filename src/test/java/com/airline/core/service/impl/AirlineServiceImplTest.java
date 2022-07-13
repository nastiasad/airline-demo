package com.airline.core.service.impl;

import com.airline.core.dto.AircraftRequest;
import com.airline.core.dto.AirlineRequest;
import com.airline.core.dto.DestinationRequest;
import com.airline.core.dto.LocationDto;
import com.airline.core.entity.AircraftEntity;
import com.airline.core.entity.AirlineEntity;
import com.airline.core.entity.DestinationEntity;
import com.airline.core.exception.AlreadyExistsException;
import com.airline.core.exception.NotFoundException;
import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;
import com.airline.core.repository.AircraftRepository;
import com.airline.core.repository.AirlineRepository;
import com.airline.core.repository.DestinationRepository;
import com.airline.core.service.AircraftMapper;
import com.airline.core.service.AirlineMapper;
import com.airline.core.service.DestinationMapper;
import liquibase.repackaged.org.apache.commons.lang3.reflect.FieldUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private AircraftRepository aircraftRepository;
    @Mock
    private DestinationRepository destinationRepository;

    final AirlineMapper airlineMapper = Mappers.getMapper(AirlineMapper.class);
    final DestinationMapper destinationMapper = Mappers.getMapper(DestinationMapper.class);
    final AircraftMapper aircraftMapper = Mappers.getMapper(AircraftMapper.class);

    @InjectMocks
    private AirlineServiceImpl airlineService;

    @BeforeEach
    @SneakyThrows
    void before() {
        FieldUtils.writeField(airlineService, "destinationMapper", destinationMapper, true);
        FieldUtils.writeField(airlineMapper, "destinationMapper", destinationMapper, true);
        FieldUtils.writeField(airlineService, "airlineMapper", airlineMapper, true);
        FieldUtils.writeField(airlineService, "aircraftMapper", aircraftMapper, true);
    }


    @Test
    void testCreateAirlineSuccess() {
        // given
        when(airlineRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(airlineRepository.save(any())).thenReturn(createAirlineEntity());
        when(destinationRepository.save(any())).thenReturn(createDestinationEntity());

        AirlineRequest airlineRequest = createAirlineRequest();

        // when
        Airline airline = airlineService.createAirline(airlineRequest);

        // then
        assertThat(airline, notNullValue());
        assertThat(airline.getId(), equalTo(10L));
        assertThat(airline.getCurrentBalance(), equalTo(BigDecimal.TEN));
        assertThat(airline.getName(), equalTo("Belavia"));

        assertThat(airline.getBaseLocation(), notNullValue());
        Destination baseLocation = airline.getBaseLocation();
        assertThat(baseLocation.getId(), equalTo(12L));
        assertThat(baseLocation.getName(), equalTo("Minsk"));
        assertThat(baseLocation.getLocation(), notNullValue());
        assertThat(baseLocation.getLocation().getLatitude(), equalTo(53.893009));
        assertThat(baseLocation.getLocation().getLongitude(), equalTo(27.567444));

        verify(destinationRepository, times(2)).save(any(DestinationEntity.class));
        verify(airlineRepository).save(any(AirlineEntity.class));
    }

    @Test
    void testCreateAirlineAlreadyExists() {
        // given
        when(airlineRepository.findByName(anyString())).thenReturn(Optional.of(createAirlineEntity()));
        AirlineRequest airlineRequest = createAirlineRequest();

        // when
        assertThrows(AlreadyExistsException.class, () -> airlineService.createAirline(airlineRequest));
    }

    @Test
    void getAirlineSuccess() {
        // given
        Long airlineId = 101L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.of(createAirlineEntity()));

        // when
        Airline airline = airlineService.getAirline(airlineId);

        // then
        assertThat(airline, notNullValue());
        assertThat(airline.getId(), equalTo(10L));
        assertThat(airline.getCurrentBalance(), equalTo(BigDecimal.TEN));
        assertThat(airline.getName(), equalTo("Belavia"));

        assertThat(airline.getBaseLocation(), notNullValue());
        Destination baseLocation = airline.getBaseLocation();
        assertThat(baseLocation.getId(), equalTo(12L));
        assertThat(baseLocation.getName(), equalTo("Minsk"));
        assertThat(baseLocation.getLocation(), notNullValue());
        assertThat(baseLocation.getLocation().getLatitude(), equalTo(53.893009));
        assertThat(baseLocation.getLocation().getLongitude(), equalTo(27.567444));
    }

    @Test
    void getAirlineNotFound() {
        // given
        Long airlineId = 101L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> airlineService.getAirline(airlineId));
    }

    @Test
    void getAirlinesSuccess() {
        // given
        when(airlineRepository.findAll()).thenReturn(Arrays.asList(createAirlineEntity(), createAirlineEntity2()));

        // when
        List<Airline> airlines = airlineService.getAirlines();

        // then
        assertThat(airlines.size(), equalTo(2));
    }

    @Test
    void getAirlinesEmpty() {
        // given
        when(airlineRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Airline> airlines = airlineService.getAirlines();

        // then
        assertThat(airlines.size(), equalTo(0));
    }

    @Test
    void testCreateAircraftSuccess() {
        // given
        long airlineId = 10L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.of(createAirlineEntity()));
        when(aircraftRepository.save(any(AircraftEntity.class))).thenReturn(createAircraftEntity());

        // when
        Aircraft aircraft = airlineService.createAircraft(airlineId, createAircraftRequest());

        // then
        assertThat(aircraft, notNullValue());
        assertThat(aircraft.getId(), equalTo(22L));
        assertThat(aircraft.getPrice(), equalTo(BigDecimal.valueOf(1000.50)));
        assertThat(aircraft.getMaxDistance(), equalTo(800.0));
    }

    @Test
    void testCreateAircraftAirlineNotFound() {
        // given
        long airlineId = 10L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> airlineService.createAircraft(airlineId, createAircraftRequest()));

        // then
        verifyNoInteractions(aircraftRepository);
    }

    @Test
    void testGetAircraftsSuccess() {
        // given
        long airlineId = 10L;
        when(aircraftRepository.findByAirlineId(airlineId)).thenReturn(Collections.singletonList(createAircraftEntity()));

        // when
        List<Aircraft> aircrafts = airlineService.getAircrafts(airlineId);

        // then
        assertThat(aircrafts.size(), equalTo(1));
        assertThat(aircrafts.get(0).getId(), equalTo(22L));
    }

    @Test
    void testGetAircraftsEmpty() {
        // given
        long airlineId = 10L;
        when(aircraftRepository.findByAirlineId(airlineId)).thenReturn(Collections.emptyList());

        // when
        List<Aircraft> aircrafts = airlineService.getAircrafts(airlineId);

        // then
        assertThat(aircrafts.size(), equalTo(0));
    }

    @Test
    void testCreateDestinationSuccess() {
        // given
        long airlineId = 10L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.of(createAirlineEntity()));
        when(destinationRepository.save(any(DestinationEntity.class))).thenReturn(createDestinationEntity());

        // when
        Destination destination = airlineService.createDestination(airlineId, createDestinationRequest());

        // then
        assertThat(destination, notNullValue());
        assertThat(destination.getId(), equalTo(12L));
        assertThat(destination.getName(), equalTo("Minsk"));
        assertThat(destination.getLocation(), notNullValue());
        assertThat(destination.getLocation().getLatitude(), equalTo(53.893009));
        assertThat(destination.getLocation().getLongitude(), equalTo(27.567444));
    }

    @Test
    void testCreateDestinationAirlineNotFound() {
        // given
        long airlineId = 10L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> airlineService.createDestination(airlineId, createDestinationRequest()));

        // then
        verifyNoInteractions(destinationRepository);
    }

    @Test
    void testGetDestinationsSuccess() {
        // given
        long airlineId = 10L;
        when(destinationRepository.findByAirlineId(airlineId)).thenReturn(
                Collections.singletonList(createDestinationEntity()));

        // when
        List<Destination> destinations = airlineService.getDestinations(airlineId);

        // then
        assertThat(destinations.size(), equalTo(1));
        assertThat(destinations.get(0).getId(), equalTo(12L));
    }

    @Test
    void testGetDestinationsEmpty() {
        // given
        long airlineId = 10L;
        when(destinationRepository.findByAirlineId(airlineId)).thenReturn(Collections.emptyList());

        // when
        List<Destination> destinations = airlineService.getDestinations(airlineId);

        // then
        assertThat(destinations.size(), equalTo(0));
    }

    private AirlineEntity createAirlineEntity() {
        return AirlineEntity.builder()
                .id(10L)
                .name("Belavia")
                .balance(BigDecimal.TEN)
                .baseLocation(createDestinationEntity())
                .build();
    }

    private AirlineEntity createAirlineEntity2() {
        return AirlineEntity.builder()
                .id(15L)
                .name("Lufthansa")
                .balance(BigDecimal.valueOf(100))
                .baseLocation(createDestinationEntity())
                .build();
    }

    private DestinationEntity createDestinationEntity() {
        return DestinationEntity.builder()
                .id(12L)
                .name("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
    }

    private AircraftEntity createAircraftEntity() {
        return AircraftEntity.builder()
                .id(22L)
                .price(BigDecimal.valueOf(1000.50))
                .maxDistance(800.0)
                .build();
    }

    private AirlineRequest createAirlineRequest() {
        return AirlineRequest.builder()
                .name("Belavia")
                .initialBudget(BigDecimal.TEN)
                .baseLocation(createDestinationRequest())
                .build();
    }

    private DestinationRequest createDestinationRequest() {
        return DestinationRequest.builder()
                .name("Minsk")
                .location(LocationDto.builder()
                        .latitude(53.893009)
                        .longitude(27.567444)
                        .build())
                .build();
    }

    private AircraftRequest createAircraftRequest() {
        return AircraftRequest.builder()
                .price(BigDecimal.valueOf(1000.50))
                .maxDistance(800.0)
                .build();
    }

}