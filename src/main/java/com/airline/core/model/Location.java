package com.airline.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Location {

    Double latitude;

    Double longitude;
}
