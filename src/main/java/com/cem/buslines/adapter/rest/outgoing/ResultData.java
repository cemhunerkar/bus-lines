package com.cem.buslines.adapter.rest.outgoing;

import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopArea;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.model.StopName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ResultData(
        // bus line properties
        @JsonProperty("LineNumber")
        Integer lineNumber,
        @JsonProperty("DirectionCode")
        Integer directionCode,
        @JsonProperty("JourneyPatternPointNumber")
        Integer journeyPointNumber,

        // Stop properties
        @JsonProperty("StopPointNumber")
        Integer stopPoint,
        @JsonProperty("StopPointName")
        String stopName,
        @JsonProperty("StopAreaTypeCode")
        String stopType,
        @JsonProperty("StopAreaNumber")
        Integer stopAreaId,

        // common properties
        @JsonProperty("LastModifiedUtcDateTime")
        String lastModifiedDate,
        @JsonProperty("ExistsFromDate")
        String existsFromDate) {

  public BusStop toBusStop() {
    StopId stopId = new StopId(stopPoint);
    StopName name = new StopName(stopName);
    StopArea stopArea = new StopArea(stopAreaId);

    return new BusStop(stopId, stopArea, name);
  }
}
