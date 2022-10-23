package com.cem.buslines.adapter.rest.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ResponseData(
        @JsonProperty("Version")
        String version,
        @JsonProperty("Type")
        String type,
        @JsonProperty("Result")
        List<ResultData> result) {
}
