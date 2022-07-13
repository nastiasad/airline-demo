package com.airline.core.dto;

import lombok.Value;

@Value
public class RouteResponse {
    DestinationSummaryResponse from;

    DestinationSummaryResponse to;

    Double distance;
}
