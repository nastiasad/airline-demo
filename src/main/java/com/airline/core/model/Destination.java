package com.airline.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Destination {
    Long id;

    String name;

    Location location;
}
