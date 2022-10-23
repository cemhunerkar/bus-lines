package com.cem.buslines.adapter.repository;

import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopArea;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.model.StopName;
import com.cem.buslines.domain.ports.TrafficDataRepository;
import com.cem.buslines.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class TrafficDataRepositoryImplTest {

  private final Map<BusNumber, BusJourney> busJourneysMap = new ConcurrentHashMap<>();

  private final Map<StopId, BusStop> busStopsMap = new ConcurrentHashMap<>();

  private final TrafficDataRepository trafficDataRepository = new TrafficDataRepositoryImpl(busJourneysMap, busStopsMap);

  @BeforeEach
  void setUp() {
    busStopsMap.clear();
    busJourneysMap.clear();
  }

  @Test
  void should_store_bus_journeys() {
    List<BusJourney> busJourneys = TestData.testBusJourneys();
    int expectedSize = busJourneys.size();

    trafficDataRepository.storeBusJourneyData(busJourneys);
    Collection<BusJourney> actual = busJourneysMap.values();

    assertThat(actual).hasSize(expectedSize);
  }

  @Test
  void should_not_store_journeys_when_it_does_not_exist_anymore() {
    List<BusJourney> busJourneys = TestData.testBusJourneys();
    busJourneys.forEach(journey -> busJourneysMap.put(journey.busNumber(), journey));
    List<BusJourney> refreshedData = List.of(
            new BusJourney(new BusNumber(1), List.of()),
            new BusJourney(new BusNumber(2), List.of()),
            new BusJourney(new BusNumber(3), List.of()));
    int expectedSize = refreshedData.size();

    trafficDataRepository.storeBusJourneyData(refreshedData);
    Collection<BusJourney> actual = busJourneysMap.values();

    assertThat(actual).hasSize(expectedSize);
  }

  @Test
  void should_store_bus_stops() {
    int count = 5;
    List<BusStop> busStops = TestData.testBusStops(count);

    trafficDataRepository.storeBusStopData(busStops);
    Collection<BusStop> actual = busStopsMap.values();

    assertThat(actual).hasSize(count);
  }

  @Test
  void should_not_store_bus_stops_when_it_does_not_exist_anymore() {
    List<BusStop> busStops = TestData.testBusStops(5);
    busStops.forEach(stop -> busStopsMap.put(stop.stopId(), stop));
    List<BusStop> refreshedData = List.of(
            new BusStop(new StopId(10001), new StopArea(10001), new StopName("Stadshagsplan")),
            new BusStop(new StopId(10002), new StopArea(10002), new StopName("Nockeby")));
    int expectedSize = refreshedData.size();

    trafficDataRepository.storeBusStopData(refreshedData);
    Collection<BusStop> actual = busStopsMap.values();

    assertThat(actual).hasSize(expectedSize);
  }

  @Test
  void should_get_bus_journeys() {
    List<BusJourney> busJourneys = TestData.testBusJourneys();
    busJourneys.forEach(journey -> busJourneysMap.put(journey.busNumber(), journey));
    int expectedSize = busJourneys.size();
    BusNumber expected = new BusNumber(1);

    List<BusJourney> actual = trafficDataRepository.getBusJourneys();

    assertThat(actual).hasSize(expectedSize)
            .first()
            .extracting(BusJourney::busNumber).isEqualTo(expected);
  }

  @Test
  void should_get_bus_stops_by_bus_journey() {
    List<BusStop> busStops = TestData.testBusStops(10);
    busStops.forEach(stop -> busStopsMap.put(stop.stopId(), stop));
    BusJourney busJourney = new BusJourney(new BusNumber(1), List.of(new StopId(10001), new StopId(10002)));

    List<BusStop> actual = trafficDataRepository.getBusStops(busJourney);

    assertThat(actual).hasSize(2);
  }

  @Test
  void should_get_bus_stop_by_id() {
    List<BusStop> busStops = TestData.testBusStops(10);
    busStops.forEach(stop -> busStopsMap.put(stop.stopId(), stop));
    StopId stopId = new StopId(10001);
    StopArea stopArea = new StopArea(10001);
    StopName expectedName = new StopName("Stadshagsplan");
    BusStop expected = new BusStop(stopId, stopArea, expectedName);

    BusStop actual = trafficDataRepository.getBusStop(stopId);

    assertThat(actual).isEqualTo(expected);
  }
}
