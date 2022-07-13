package com.airline.core.dto;

import lombok.Value;

@Value
public class DestinationResponse {
    Long id;

    String name;

    LocationDto location;
}
