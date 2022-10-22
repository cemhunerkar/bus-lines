package com.cem.buslines.domain.ports;

import com.cem.buslines.adapter.rest.outgoing.TrafficDataClient;
import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusLine;
import com.cem.buslines.domain.model.BusStop;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class BusLinesService {

  private final TrafficDataClient trafficDataClient;
  private final TrafficDataRepository trafficDataRepository;

  public BusLinesService(TrafficDataClient trafficDataClient, TrafficDataRepository trafficDataRepository) {
    this.trafficDataClient = trafficDataClient;
    this.trafficDataRepository = trafficDataRepository;
  }

  public void fetchAndStoreJourneyData() {
    trafficDataClient.fetchBusJourneys()
            .subscribe(trafficDataRepository::storeBusJourneyData);
    trafficDataClient.fetchBusStops()
            .subscribe(trafficDataRepository::storeBusStopData);
  }

  public List<BusLine> getBusLinesWithMostStops(int numberOfResults) {
    List<BusJourney> busJourneys = trafficDataRepository.getBusJourneys();
    Comparator<BusLine> reverseOrderComparator = (b1, b2) -> Integer.compare(b2.stops().size(), b1.stops().size());
    return busJourneys.stream()
            .map(journey -> {
              List<BusStop> busStops = trafficDataRepository.getBusStops(journey);
              List<BusStop> stops = busStops.stream()
                      .filter(Objects::nonNull)
                      .filter(distinctByKey(BusStop::stopArea))
                      .toList();
              return new BusLine(journey.busNumber(), stops);
            })
            .sorted(reverseOrderComparator)
            .limit(numberOfResults)
            .toList();
  }

  /*
    One bus stop can have multiple stopIds, they share the stopArea id.
    We can use that to find distinct bus stops.
   */
  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
