package com.airline.core.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Aircraft {
    Long id;

    Long airlineId;

    BigDecimal price;

    Double maxDistance;
}
