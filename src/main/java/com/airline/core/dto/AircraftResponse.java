package com.airline.core.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class AircraftResponse {
    Long id;

    Long airlineId;

    BigDecimal price;

    Double maxDistance;
}
