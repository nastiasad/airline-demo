package com.airline.core.dto;

import lombok.*;

@Value
@Builder
public class FieldErrorResponse {
    String field;
    String error;
    String value;
}
