package com.airline.core.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class LocationDto {
    @NotNull
    Double latitude;

    @NotNull
    Double longitude;
}
