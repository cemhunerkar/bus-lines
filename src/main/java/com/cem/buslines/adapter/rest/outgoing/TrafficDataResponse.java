package com.cem.buslines.adapter.rest.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TrafficDataResponse(
        @JsonProperty("StatusCode")
        Integer statusCode,
        @JsonProperty("Message")
        String message,
        @JsonProperty("ExecutionTime")
        Integer executionTime,
        @JsonProperty("ResponseData")
        ResponseData responseData
) {
}
