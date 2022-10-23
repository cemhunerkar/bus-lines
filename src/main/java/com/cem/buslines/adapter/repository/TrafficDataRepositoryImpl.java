package com.cem.buslines.adapter.repository;

import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.ports.TrafficDataRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class TrafficDataRepositoryImpl implements TrafficDataRepository {

  private final Map<BusNumber, BusJourney> busJourneysMap;

  private final Map<StopId, BusStop> busStopsMap;

  public TrafficDataRepositoryImpl(Map<BusNumber, BusJourney> busJourneysMap, Map<StopId, BusStop> busStopsMap) {
    this.busJourneysMap = busJourneysMap;
    this.busStopsMap = busStopsMap;
  }

  @Override
  public void storeBusStopData(List<BusStop> busStops) {
    log.debug("Storing {} bus stops", busStops.size());
    Map<StopId, BusStop> newData = busStops.stream()
            .collect(Collectors.toMap(BusStop::stopId, Function.identity()));
    // Cannot perform .clear() and .putAll(), if a request comes in between, result will be empty
    // Instead we put everything and remove the ones that does not exist in the new data.
    busStopsMap.putAll(newData);
    busStopsMap.keySet().removeIf(key -> !newData.containsKey(key));
  }

  @Override
  public void storeBusJourneyData(List<BusJourney> busJourneys) {
    log.debug("Storing {} bus journeys", busJourneys.size());
    Map<BusNumber, BusJourney> newData = busJourneys.stream()
            .collect(Collectors.toMap(BusJourney::busNumber, Function.identity()));
    // Cannot perform .clear() and .putAll(), if a request comes in between, result will be empty
    // Instead we put everything and remove the ones that does not exist in the new data.
    busJourneysMap.putAll(newData);
    busJourneysMap.keySet().removeIf(key -> !newData.containsKey(key));
  }

  @Override
  public List<BusJourney> getBusJourneys() {
    log.debug("Getting all bus journeys");
    return busJourneysMap.values().stream().toList();
  }

  @Override
  public List<BusStop> getBusStops(BusJourney busJourney) {
    return busJourney.stops().stream()
            .map(this::getBusStop)
            .toList();
  }

  @Override
  public BusStop getBusStop(StopId stopId) {
    return busStopsMap.get(stopId);
  }
}
