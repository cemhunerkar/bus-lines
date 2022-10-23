package com.cem.buslines.configuration;

import com.cem.buslines.adapter.rest.incoming.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  public ErrorResponse handleValidationException(ValidationException e) {
    log.error("Handling validation exception", e);
    return new ErrorResponse(10000, e.getMessage());
  }

  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(ExternalClientException.class)
  public ErrorResponse handleExternalClientException(ExternalClientException e) {
    log.error("Handling external client exception", e);
    return new ErrorResponse(10001, e.getMessage());
  }
}
