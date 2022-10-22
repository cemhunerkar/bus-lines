package com.cem.buslines.configuration;

import com.cem.buslines.adapter.rest.incoming.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  public ErrorResponse handleValidationException(ValidationException e) {
    return new ErrorResponse(10000, e.getMessage());
  }
}
