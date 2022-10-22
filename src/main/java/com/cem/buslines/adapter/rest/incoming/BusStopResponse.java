package com.cem.buslines.adapter.rest.incoming;

import com.cem.buslines.domain.model.BusStop;

public record BusStopResponse(Integer stopAreaId, String stopName) {

  public static BusStopResponse fromDomain(BusStop busStop) {
    Integer id = busStop.stopArea().areaId();
    String name = busStop.stopName().name();

    return new BusStopResponse(id, name);
  }
}
