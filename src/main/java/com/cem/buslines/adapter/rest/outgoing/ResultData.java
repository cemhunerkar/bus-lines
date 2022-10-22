package com.cem.buslines.adapter.rest.outgoing;

import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopArea;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.model.StopName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultData {

  // bus line properties
  @JsonProperty("LineNumber")
  private Integer lineNumber;
  @JsonProperty("DirectionCode")
  private Integer directionCode;
  @JsonProperty("JourneyPatternPointNumber")
  private Integer journeyPointNumber;

  // Stop properties
  @JsonProperty("StopPointNumber")
  private Integer stopPoint;
  @JsonProperty("StopPointName")
  private String stopName;
  @JsonProperty("StopAreaTypeCode")
  private String stopType;
  @JsonProperty("StopAreaNumber")
  private Integer stopAreaId;

  // common properties
  @JsonProperty("LastModifiedUtcDateTime")
  private String lastModifiedDate;
  @JsonProperty("ExistsFromDate")
  private String existsFromDate;

  public BusStop toBusStop() {
    StopId stopId = new StopId(stopPoint);
    StopName name = new StopName(stopName);
    StopArea stopArea = new StopArea(stopAreaId);

    return new BusStop(stopId, stopArea, name);
  }

}
