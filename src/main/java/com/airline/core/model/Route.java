package com.airline.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Route {

    Destination from;

    Destination to;

    double haversineDistance;

}
