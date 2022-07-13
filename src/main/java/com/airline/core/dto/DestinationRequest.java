package com.airline.core.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Value
@Builder
public class DestinationRequest {
    @NotBlank
    String name;

    @Valid
    LocationDto location;
}
