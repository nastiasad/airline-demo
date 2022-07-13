package com.airline.core.model;

import lombok.Value;

@Value
public class Destination {
    Long id;

    String name;

    Location location;
}
