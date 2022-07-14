package com.airline.core.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineRequest {
    @NotBlank
    String name;

    @PositiveOrZero
    BigDecimal initialBudget;

    @Valid
    @NotNull
    DestinationRequest baseLocation;
}
