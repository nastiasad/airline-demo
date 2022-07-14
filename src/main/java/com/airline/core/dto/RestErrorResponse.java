package com.airline.core.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RestErrorResponse {
    String code;

    String message;

    String description;

    List<FieldErrorResponse> errors;
}
