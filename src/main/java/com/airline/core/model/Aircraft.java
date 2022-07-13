package com.airline.core.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class Aircraft {
    Long id;

    Long airlineId;

    BigDecimal price;

    Double maxDistance;

    LocalDate creationDate;
}
