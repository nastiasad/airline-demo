package com.airline.core.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationRequest {
    @NotBlank
    String name;

    @Valid
    @NotNull
    LocationDto location;
}
