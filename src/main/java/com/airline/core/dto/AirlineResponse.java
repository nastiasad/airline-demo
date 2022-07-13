package com.airline.core.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class AirlineResponse {
    Long id;

    String name;

    BigDecimal currentBalance;

    DestinationResponse baseLocation;
}
