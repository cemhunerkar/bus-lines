package com.cem.buslines.adapter.rest.outgoing;

import com.cem.buslines.configuration.ExternalClientException;
import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class TrafficDataClient {

  private static final String API_PATH = "/api2/LineData.json";

  private static final String JOURNEY_MODEL = "jour";

  private static final String STOP_MODEL = "stop";

  private static final String BUS_TERMINAL = "BUSTERM";

  private final WebClient webClient;

  private final String apiKey;

  public TrafficDataClient(WebClient webClient, String apiKey) {
    this.webClient = webClient;
    this.apiKey = apiKey;
  }

  public Mono<List<BusStop>> fetchBusStops() {
    log.debug("Fetching stop data");
    return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(API_PATH)
                    .queryParam("model", STOP_MODEL)
                    .queryParam("key", apiKey)
                    .build())
            .retrieve()
            .bodyToMono(TrafficDataResponse.class)
            .retry(3)
            .onErrorMap(ExternalClientException::new)
            .map(TrafficDataResponse::responseData)
            .map(ResponseData::result)
            .flatMapMany(Flux::fromIterable)
            .filter(stop -> stop.stopType().equals(BUS_TERMINAL))
            .map(ResultData::toBusStop)
            .collectList();
  }

  public Mono<List<BusJourney>> fetchBusJourneys() {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(API_PATH)
                    .queryParam("model", JOURNEY_MODEL)
                    .queryParam("key", apiKey)
                    .queryParam("DefaultTransportModeCode", "BUS")
                    .build())
            .retrieve()
            .bodyToMono(TrafficDataResponse.class)
            .retry(3)
            .onErrorMap(ExternalClientException::new)
            .map(this::getBusJourneysAndGroup);
  }

  private List<BusJourney> getBusJourneysAndGroup(TrafficDataResponse trafficDataResponse) {
    List<ResultData> results = Optional.ofNullable(trafficDataResponse)
            .map(TrafficDataResponse::responseData)
            .map(ResponseData::result)
            .orElseThrow();

    return results.stream()
            .filter(line -> line.directionCode() == 1)
            .collect(
                    groupingBy(ResultData::lineNumber, Collectors.mapping(ResultData::journeyPointNumber, Collectors.toSet())))
            .entrySet().stream()
            .map(this::entryToBusJourney)
            .toList();
  }

  private BusJourney entryToBusJourney(Map.Entry<Integer, Set<Integer>> entry) {
    BusNumber busNumber = new BusNumber(entry.getKey());
    List<StopId> stopIds = entry.getValue().stream().map(StopId::new).toList();

    return new BusJourney(busNumber, stopIds);
  }
}
