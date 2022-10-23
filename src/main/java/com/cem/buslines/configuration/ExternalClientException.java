package com.cem.buslines.configuration;

public class ExternalClientException extends RuntimeException {

  public ExternalClientException(Throwable cause) {
    super("Problem occurred in external call", cause);
  }
}
