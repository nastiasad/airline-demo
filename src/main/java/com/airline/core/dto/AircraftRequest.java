package com.airline.core.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Value
@Builder
public class AircraftRequest {
    @PositiveOrZero
    BigDecimal price;

    @PositiveOrZero
    Double maxDistance;
}
