package com.airline.core.service.impl;

import com.airline.core.dto.*;
import com.airline.core.entity.AircraftEntity;
import com.airline.core.entity.AirlineEntity;
import com.airline.core.entity.DestinationEntity;
import com.airline.core.exception.BadRequestException;
import com.airline.core.exception.NotFoundException;
import com.airline.core.model.Destination;
import com.airline.core.repository.AircraftRepository;
import com.airline.core.repository.AirlineRepository;
import com.airline.core.repository.DestinationRepository;
import com.airline.core.service.AircraftMapper;
import com.airline.core.service.AirlineMapper;
import com.airline.core.service.DestinationMapper;
import com.airline.core.service.TimeService;
import liquibase.repackaged.org.apache.commons.lang3.reflect.FieldUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    @Mock
    private TimeService timeService;

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

        var airlineRequest = createAirlineRequest();

        // when
        var airline = airlineService.createAirline(airlineRequest);

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
        var airlineRequest = createAirlineRequest();

        // when
        assertThrows(BadRequestException.class, () -> airlineService.createAirline(airlineRequest));
    }

    @Test
    void getAirlineSuccess() {
        // given
        Long airlineId = 101L;
        when(airlineRepository.findById(airlineId)).thenReturn(Optional.of(createAirlineEntity()));

        // when
        var airline = airlineService.getAirline(airlineId);

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
        var airlines = airlineService.getAirlines();

        // then
        assertThat(airlines.size(), equalTo(2));
    }

    @Test
    void getAirlinesEmpty() {
        // given
        when(airlineRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        var airlines = airlineService.getAirlines();

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
        var aircraft = airlineService.createAircraft(airlineId, createAircraftRequest());

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
        var aircrafts = airlineService.getAircrafts(airlineId);

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
        var aircrafts = airlineService.getAircrafts(airlineId);

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
        var destination = airlineService.createDestination(airlineId, createDestinationRequest());

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
        var destinations = airlineService.getDestinations(airlineId);

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
        var destinations = airlineService.getDestinations(airlineId);

        // then
        assertThat(destinations.size(), equalTo(0));
    }

    @Test
    void testSellAircraftSuccess() {
        // given
        Long aircraftId = 1L;
        var aircraftEntity = createAircraftEntity(BigDecimal.valueOf(1000.50),
                LocalDate.of(2019, 1, 1));
        when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraftEntity));
        when(timeService.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));

        // when
        airlineService.sellAircraft(aircraftId);

        // then
        verify(aircraftRepository).delete(aircraftEntity);

        var airlineCaptor = ArgumentCaptor.forClass(AirlineEntity.class);
        verify(airlineRepository).save(airlineCaptor.capture());
        var capturedAirline = airlineCaptor.getValue();
        assertThat(capturedAirline.getBalance(), comparesEqualTo(BigDecimal.valueOf(290.140)));
    }

    @Test
    void testSellAircraftAircraftPriceNegative() {
        // given
        Long aircraftId = 1L;
        var aircraftEntity = createAircraftEntity(BigDecimal.valueOf(1000.50),
                LocalDate.of(2015, 1, 1));
        when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraftEntity));
        when(timeService.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));

        // when
        assertThrows(BadRequestException.class, () -> airlineService.sellAircraft(aircraftId));

        // then
        verifyNoInteractions(airlineRepository);
        verifyNoMoreInteractions(aircraftRepository);
    }

    @Test
    void testBuyAircraftSuccess() {
        // given
        Long aircraftId = 1L;
        Long buyerAirlineId = 2L;
        var buyerAirlineRequest = BuyerAirlineRequest.builder()
                .buyerAirlineId(buyerAirlineId)
                .build();

        when(timeService.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));
        when(airlineRepository.findById(buyerAirlineId)).thenReturn(Optional.of(createAirlineEntity2(BigDecimal.valueOf(600))));
        var aircraftToBuyEntity = createAircraftEntity(BigDecimal.valueOf(1000.50),
                LocalDate.of(2020, 1, 1));
        when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraftToBuyEntity));

        // when
        airlineService.buyAircraft(aircraftId, buyerAirlineRequest);

        // then
        var airlineCaptor = ArgumentCaptor.forClass(AirlineEntity.class);
        verify(airlineRepository, times(2)).save(airlineCaptor.capture());
        var allCapturedAirlines = airlineCaptor.getAllValues();
        assertThat(allCapturedAirlines.get(0).getName(), equalTo("Lufthansa"));
        assertThat(allCapturedAirlines.get(0).getBalance(), comparesEqualTo(BigDecimal.valueOf(79.740)));
        assertThat(allCapturedAirlines.get(1).getName(), equalTo("Belavia"));
        assertThat(allCapturedAirlines.get(1).getBalance(), comparesEqualTo(BigDecimal.valueOf(530.260)));

        var aircraftCaptor = ArgumentCaptor.forClass(AircraftEntity.class);
        verify(aircraftRepository).save(aircraftCaptor.capture());
        assertThat(aircraftCaptor.getValue().getAirline().getName(), equalTo(allCapturedAirlines.get(0).getName()));
    }

    @Test
    void testBuyAircraftNotEnoughMoney() {
        // given
        Long aircraftId = 1L;
        Long buyerAirlineId = 2L;
        var buyerAirlineRequest = BuyerAirlineRequest.builder()
                .buyerAirlineId(buyerAirlineId)
                .build();

        when(timeService.getCurrentDate()).thenReturn(LocalDate.of(2022, 1, 1));
        when(airlineRepository.findById(buyerAirlineId)).thenReturn(Optional.of(createAirlineEntity2(BigDecimal.valueOf(600))));
        var aircraftToBuyEntity = createAircraftEntity(BigDecimal.valueOf(5000.50),
                LocalDate.of(2020, 1, 1));
        when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraftToBuyEntity));

        // when
        assertThrows(BadRequestException.class, () -> airlineService.buyAircraft(aircraftId, buyerAirlineRequest));

        // then
        verify(aircraftRepository, times(0)).save(any(AircraftEntity.class));
        verify(airlineRepository, times(0)).save(any(AirlineEntity.class));
    }

    @Test
    void testBuyAircraftAlreadyBelongs() {
        // given
        Long aircraftId = 22L;
        Long buyerAirlineId = 10L;
        var buyerAirlineRequest = BuyerAirlineRequest.builder()
                .buyerAirlineId(buyerAirlineId)
                .build();

        when(airlineRepository.findById(buyerAirlineId)).thenReturn(Optional.of(createAirlineEntity()));
        var aircraftToBuyEntity = createAircraftEntity(BigDecimal.valueOf(5000.50),
                LocalDate.of(2020, 1, 1));
        when(aircraftRepository.findById(aircraftId)).thenReturn(Optional.of(aircraftToBuyEntity));

        // when
        assertThrows(BadRequestException.class, () -> airlineService.buyAircraft(aircraftId, buyerAirlineRequest));

        // then
        verify(aircraftRepository, times(0)).save(any(AircraftEntity.class));
        verify(airlineRepository, times(0)).save(any(AirlineEntity.class));
    }

    private AirlineEntity createAirlineEntity() {
        return AirlineEntity.builder()
                .id(10L)
                .name("Belavia")
                .balance(BigDecimal.TEN)
                .baseLocation(createDestinationEntity())
                .build();
    }

    private AirlineEntity createAirlineEntity2(BigDecimal balance) {
        return AirlineEntity.builder()
                .id(15L)
                .name("Lufthansa")
                .balance(balance)
                .baseLocation(createDestinationEntity())
                .build();
    }

    private AirlineEntity createAirlineEntity2() {
        return createAirlineEntity2(BigDecimal.valueOf(100));
    }

    private DestinationEntity createDestinationEntity() {
        return DestinationEntity.builder()
                .id(12L)
                .name("Minsk")
                .latitude(53.893009)
                .longitude(27.567444)
                .build();
    }

    private AircraftEntity createAircraftEntity(BigDecimal price, LocalDate currentDate) {
        return AircraftEntity.builder()
                .id(22L)
                .price(price)
                .maxDistance(800.0)
                .airline(createAirlineEntity())
                .creationDate(currentDate)
                .build();
    }

    private AircraftEntity createAircraftEntity() {
        return createAircraftEntity(BigDecimal.valueOf(1000.50),
                LocalDate.of(2019, 1, 1));
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