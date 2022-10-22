package com.cem.buslines.configuration;

import com.cem.buslines.adapter.repository.TrafficDataRepositoryImpl;
import com.cem.buslines.adapter.rest.outgoing.TrafficDataClient;
import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.ports.BusLinesService;
import com.cem.buslines.domain.ports.TrafficDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class BusLinesConfiguration {

  @Bean
  public BusLinesService busLinesService(TrafficDataClient trafficDataClient, TrafficDataRepository trafficDataRepository) {
    return new BusLinesService(trafficDataClient, trafficDataRepository);
  }

  @Bean
  public TrafficDataClient trafficDataClient(WebClient webClient, @Value("${client.sl.api-key}") String apiKey) {
    return new TrafficDataClient(webClient, apiKey);
  }

  @Bean
  public TrafficDataRepository trafficDataRepository(Map<BusNumber, BusJourney> busJourneysMap, Map<StopId, BusStop> busStopsMap) {
    return new TrafficDataRepositoryImpl(busJourneysMap, busStopsMap);
  }

  @Bean
  public Map<BusNumber, BusJourney> busJourneysMap() {
    // Can be replaced with Hazelcast IMap or Redis Map
    return new ConcurrentHashMap<>();
  }

  @Bean
  public Map<StopId, BusStop> busStopsMap() {
    // Can be replaced with Hazelcast IMap or Redis Map
    return new ConcurrentHashMap<>();
  }

}
