package com.airline.core.service.impl;

import com.airline.core.dto.*;
import com.airline.core.entity.AircraftEntity;
import com.airline.core.entity.AirlineEntity;
import com.airline.core.entity.DestinationEntity;
import com.airline.core.exception.BadRequestException;
import com.airline.core.exception.NotFoundException;
import com.airline.core.model.Aircraft;
import com.airline.core.model.Airline;
import com.airline.core.model.Destination;
import com.airline.core.repository.AircraftRepository;
import com.airline.core.repository.AirlineRepository;
import com.airline.core.repository.DestinationRepository;
import com.airline.core.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;
    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;
    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;
    private final TimeService timeService;

    @Override
    @Transactional
    public Airline createAirline(AirlineRequest airlineRequest) {
        if (airlineRepository.findByName(airlineRequest.getName()).isPresent()) {
            throw new BadRequestException(String.format("Airline with the same name %s already exists",
                    airlineRequest.getName()));
        }

        var destinationEntity = this.createDestinationEntity(airlineRequest.getBaseLocation());

        log.info("Creating airline with airline name :: {}", airlineRequest.getName());
        AirlineEntity airlineToSave = airlineMapper.map(airlineRequest);
        airlineToSave.setBaseLocation(destinationEntity);
        AirlineEntity airlineCreated = airlineRepository.save(airlineToSave);

        log.info("Updating destination-airline relation for destination name :: {}, airline name :: {}",
                destinationEntity.getName(), airlineCreated.getName());
        destinationEntity.addAirline(airlineCreated);
        destinationRepository.save(destinationEntity);

        return airlineMapper.map(airlineCreated);
    }

    @Override
    public Airline getAirline(Long airlineId) {
        log.info("Getting the airline for airline id :: {}", airlineId);
        AirlineEntity airlineEntity = getAirlineEntity(airlineId);
        return airlineMapper.map(airlineEntity);
    }

    @Override
    public List<Airline> getAirlines() {
        log.info("Getting all the airlines");
        List<AirlineEntity> airlines = airlineRepository.findAll();
        return airlineMapper.map(airlines);
    }

    @Override
    public Aircraft createAircraft(Long airlineId, AircraftRequest aircraftRequest) {
        log.info("Creating the aircraft for the airline with id :: {}, aircraftRequest :: {}",
                airlineId, aircraftRequest);
        AirlineEntity airlineExisting = airlineRepository.findById(airlineId).orElseThrow(() -> new NotFoundException(
                String.format("Error finding the existing airline by id %s", airlineId)));
        AircraftEntity aircraftEntity = aircraftMapper.map(aircraftRequest);
        aircraftEntity.setAirline(airlineExisting);

        AircraftEntity aircraftCreated = aircraftRepository.save(aircraftEntity);
        return aircraftMapper.map(aircraftCreated);

    }

    @Override
    public List<Aircraft> getAircrafts(Long airlineId) {
        log.info("Getting all the aircrafts for airline id :: {}", airlineId);
        List<AircraftEntity> aircraftEntities = aircraftRepository.findByAirlineId(airlineId);
        return aircraftMapper.map(aircraftEntities);
    }

    @Override
    public Destination createDestination(Long airlineId, DestinationRequest destinationRequest) {
        log.info("Creating the destination for airline with id :: {}, destinationRequest :: {}",
                airlineId, destinationRequest);
        AirlineEntity airlineExisting = getAirlineEntity(airlineId);

        DestinationEntity destinationEntity = destinationRepository.findByLatitudeAndLongitude(
                        destinationRequest.getLocation().getLatitude(),
                        destinationRequest.getLocation().getLongitude())
                .orElseGet(() -> destinationMapper.map(destinationRequest));
        destinationEntity.getAirlines().add(airlineExisting);
        DestinationEntity destinationCreated = destinationRepository.save(destinationEntity);

        return destinationMapper.map(destinationCreated);
    }

    private DestinationEntity createDestinationEntity(DestinationRequest destinationRequest) {
        log.info("Creating destination for destinationRequest :: {}", destinationRequest);
        var destinationEntity = destinationRepository.findByLatitudeAndLongitude(
                        destinationRequest.getLocation().getLatitude(),
                        destinationRequest.getLocation().getLongitude())
                .orElseGet(() -> destinationMapper.map(destinationRequest));
        return destinationRepository.save(destinationEntity);
    }

    @Override
    public List<Destination> getDestinations(Long airlineId) {
        log.info("Getting all the destinations for airline with id :: {}", airlineId);
        List<DestinationEntity> destinationEntities = destinationRepository.findByAirlineId(airlineId);
        return destinationMapper.map(destinationEntities);
    }

    @Override
    @Transactional
    public void sellAircraft(Long aircraftId) {
        log.info("Selling the aircraft with id :: {}", aircraftId);
        AircraftEntity aircraftToSell = this.getAircraftEntity(aircraftId);

        BigDecimal sellPrice = calculateSellPrice(aircraftToSell);
        log.info("The sell price was calculated for the aircraft with id :: {}, price :: {}",
                aircraftId, sellPrice);
        if (sellPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("The aircraft with id %s has negative sell price and cannot be sold");
        }
        AirlineEntity airlineEntity = aircraftToSell.getAirline();
        log.info("Deleting the aircraft with id :: {} from the airline with id :: {}",
                aircraftId, airlineEntity.getId());
        aircraftRepository.delete(aircraftToSell);

        log.info("Updating the balance for the airline with id :: {}", airlineEntity.getId());
        airlineEntity.addToBalance(sellPrice);
        airlineRepository.save(airlineEntity);
    }

    private BigDecimal calculateSellPrice(AircraftEntity aircraft) {
        return aircraft.getPrice().multiply(
                BigDecimal.valueOf((1 - getMonthsInUse(aircraft.getCreationDate(), timeService.getCurrentDate()) * 0.02))
        );
    }

    private long getMonthsInUse(LocalDate creationDate, LocalDate now) {
        return ChronoUnit.MONTHS.between(
                YearMonth.from(creationDate),
                YearMonth.from(now));
    }

    @Override
    @Transactional
    public void buyAircraft(Long aircraftId, BuyerAirlineRequest buyerAirlineRequest) {
        log.info("Buying the aircraft with id :: {} for the buyer airline with id :: {}", aircraftId,
                buyerAirlineRequest.getBuyerAirlineId());
        AirlineEntity buyerAirlineEntity = this.getAirlineEntity(buyerAirlineRequest.getBuyerAirlineId());
        AircraftEntity aircraftToBuy = this.getAircraftEntity(aircraftId);

        if (aircraftBelongsToAirline(buyerAirlineEntity, aircraftToBuy)) {
            throw new BadRequestException(String.format(
                    "The aircraft with id %s already belongs to the airline with id %s",
                    aircraftId, buyerAirlineRequest.getBuyerAirlineId()));
        }

        BigDecimal sellPrice = calculateSellPrice(aircraftToBuy);
        log.info("The sell price was calculated for the aircraft with id :: {}, price :: {}",
                aircraftId, sellPrice);
        if (buyerAirlineEntity.getBalance().compareTo(sellPrice) < 0) {
            throw new BadRequestException(String.format(
                    "The airline with id %s doesn't have enough money to buy the aircraft with id %s",
                    buyerAirlineRequest.getBuyerAirlineId(), aircraftId));
        }

        log.info("Updating the balance for the buyer airline with id :: {}", buyerAirlineEntity.getId());
        buyerAirlineEntity.addToBalance(sellPrice.negate());
        airlineRepository.save(buyerAirlineEntity);

        AirlineEntity airlineToBuyFrom = aircraftToBuy.getAirline();
        log.info("Updating the balance for the seller airline with id :: {}", airlineToBuyFrom.getId());
        airlineToBuyFrom.addToBalance(sellPrice);
        airlineRepository.save(airlineToBuyFrom);

        log.info("Setting the aircraft relation for the aircraft with id :: {} to the buyer airline with id : {}",
                aircraftId, buyerAirlineEntity.getId());
        aircraftToBuy.setAirline(buyerAirlineEntity);
        aircraftRepository.save(aircraftToBuy);
    }

    private static boolean aircraftBelongsToAirline(AirlineEntity airlineEntity, AircraftEntity aircraftEntity) {
        return aircraftEntity.getAirline().getId().equals(airlineEntity.getId());
    }

    private AirlineEntity getAirlineEntity(Long airlineId) {
        return airlineRepository.findById(airlineId).orElseThrow(() -> new NotFoundException(
                String.format("Error finding the existing airline by id %s", airlineId)));
    }

    private AircraftEntity getAircraftEntity(Long aircraftId) {
        return aircraftRepository.findById(aircraftId).orElseThrow(() -> new NotFoundException(
                String.format("Error finding the existing aircraft by id %s", aircraftId)));
    }

}
