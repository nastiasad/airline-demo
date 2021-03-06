package com.airline.core.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull
    Double latitude;

    @NotNull
    Double longitude;
}
