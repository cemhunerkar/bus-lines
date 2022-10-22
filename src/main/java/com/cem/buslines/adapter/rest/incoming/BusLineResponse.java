package com.cem.buslines.adapter.rest.incoming;

import com.cem.buslines.domain.model.BusLine;
import com.cem.buslines.domain.model.BusStop;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BusLineResponse(Integer busNumber, Integer stopCount, List<BusStopResponse> busStops) {

  public static BusLineResponse fromDomain(BusLine busLine) {
    List<BusStop> stopList = busLine.stops().stream()
            .collect(Collectors.toMap(BusStop::stopArea, Function.identity(), (a, b) -> a))
            .values().stream().toList();
    List<BusStopResponse> busStops = stopList.stream()
            .filter(Objects::nonNull)
            .map(BusStopResponse::fromDomain).toList();
    Integer number = busLine.busNumber().number();

    return new BusLineResponse(number, stopList.size(), busStops);
  }
}
