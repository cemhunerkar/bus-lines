package com.cem.buslines.adapter.rest.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrafficDataResponse {

  @JsonProperty("StatusCode")
  private Integer statusCode;
  @JsonProperty("Message")
  private String message;
  @JsonProperty("ExecutionTime")
  private Integer executionTime;
  @JsonProperty("ResponseData")
  private ResponseData responseData;

}
