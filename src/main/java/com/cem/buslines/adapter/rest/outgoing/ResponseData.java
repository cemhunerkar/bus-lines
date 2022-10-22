package com.cem.buslines.adapter.rest.outgoing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResponseData {

  @JsonProperty("Version")
  private String version;
  @JsonProperty("Type")
  private String type;
  @JsonProperty("Result")
  private List<ResultData> result;
}
