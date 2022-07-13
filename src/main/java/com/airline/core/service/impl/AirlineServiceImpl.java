package com.airline.core.service.impl;

import com.airline.core.dto.*;
import com.airline.core.entity.AirlineEntity;
import com.airline.core.entity.DestinationEntity;
import com.airline.core.exception.AlreadyExistsException;
import com.airline.core.exception.NotFoundException;
import com.airline.core.model.Airline;
import com.airline.core.repository.AirlineRepository;
import com.airline.core.repository.DestinationRepository;
import com.airline.core.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;
    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    @Override
    public Airline createAirline(AirlineRequest airlineRequest) {
        if (airlineRepository.findByName(airlineRequest.getName()).isPresent()) {
            log.error("Airline with the same name :: {} already exists.", airlineRequest.getName());
            throw new AlreadyExistsException(String.format("Airline with the same name %s already exists",
                    airlineRequest.getName()));
        }

        DestinationEntity destinationEntity = this.createDestinationEntity(airlineRequest.getBaseLocation());

        AirlineEntity airlineToSave = airlineMapper.map(airlineRequest);
        airlineToSave.setBaseLocation(destinationEntity);
        AirlineEntity airlineCreated = airlineRepository.save(airlineToSave);

        destinationEntity.addAirline(airlineCreated);
        destinationRepository.save(destinationEntity);

        return airlineMapper.map(airlineCreated);
    }

    @Override
    public Airline getAirline(Long airlineId) {
        AirlineEntity airlineEntity = getAirlineEntity(airlineId);
        return airlineMapper.map(airlineEntity);
    }

    @Override
    public List<Airline> getAirlines() {
        List<AirlineEntity> airlines = airlineRepository.findAll();
        return airlineMapper.map(airlines);
    }

    private DestinationEntity createDestinationEntity(DestinationRequest destinationRequest) {
        DestinationEntity destinationEntity = destinationRepository.findByLatitudeAndLongitude(
                        destinationRequest.getLocation().getLatitude(),
                        destinationRequest.getLocation().getLongitude())
                .orElseGet(() -> destinationMapper.map(destinationRequest));
        return destinationRepository.save(destinationEntity);
    }

    private AirlineEntity getAirlineEntity(Long airlineId) {
        return airlineRepository.findById(airlineId).orElseThrow(() -> {
            log.error("Error finding the existing airline by id :: {}", airlineId);
            return new NotFoundException(String.format("Error finding the existing airline by id %s", airlineId));
        });
    }
}
