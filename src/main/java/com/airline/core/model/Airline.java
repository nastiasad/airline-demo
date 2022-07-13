package com.airline.core.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Airline {
    Long id;

    String name;

    BigDecimal currentBalance;

    Destination baseLocation;
}
