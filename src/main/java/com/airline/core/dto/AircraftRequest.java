package com.airline.core.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftRequest {
    @NotNull
    @PositiveOrZero
    BigDecimal price;

    @NotNull
    @PositiveOrZero
    Double maxDistance;
}
