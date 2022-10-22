package com.cem.buslines.domain.ports;

import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopId;

import java.util.List;

public interface TrafficDataRepository {

  void storeBusStopData(List<BusStop> busStops);

  void storeBusJourneyData(List<BusJourney> busJourneys);

  List<BusJourney> getBusJourneys();

  List<BusStop> getBusStops(BusJourney busJourney);

  BusStop getBusStop(StopId stopId);
}
