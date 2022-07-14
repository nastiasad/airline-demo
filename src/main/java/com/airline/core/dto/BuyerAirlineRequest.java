package com.airline.core.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerAirlineRequest {
    @NotNull
    Long buyerAirlineId;
}
