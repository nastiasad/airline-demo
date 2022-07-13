package com.airline.core.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Airline {
    Long id;

    String name;

    BigDecimal currentBalance;

    Destination baseLocation;
}
