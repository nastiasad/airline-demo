package com.airline.core.exception.handler;

import com.airline.core.dto.FieldErrorResponse;
import com.airline.core.dto.RestErrorResponse;
import com.airline.core.exception.BadRequestException;
import com.airline.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<RestErrorResponse> handleNotFoundException(NotFoundException exception) {
        log.warn(exception.getMessage());
        var errorResponse = RestErrorResponse.builder()
                .message(exception.getMessage())
                .code(String.valueOf(NOT_FOUND.value()))
                .description(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<RestErrorResponse> handleBadRequestException(BadRequestException exception) {
        log.warn(exception.getMessage());
        var errorResponse = RestErrorResponse.builder()
                .message(exception.getMessage())
                .code(String.valueOf(BAD_REQUEST.value()))
                .description(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<FieldErrorResponse> errors = exception.getFieldErrors().stream()
                .map(error -> FieldErrorResponse.builder()
                        .field(error.getField())
                        .error(error.getDefaultMessage())
                        .value(String.valueOf(error.getRejectedValue()))
                        .build())
                .collect(toList());
        RestErrorResponse errorResponse = RestErrorResponse.builder()
                .code(String.valueOf(BAD_REQUEST.value()))
                .message(BAD_REQUEST.getReasonPhrase())
                .errors(errors)
                .description(String.valueOf(BAD_REQUEST.value()))
                .build();
        return new ResponseEntity<>(Collections.singletonList(errorResponse), BAD_REQUEST);
    }
}
