package com.cem.buslines.domain.ports;

import com.cem.buslines.adapter.rest.outgoing.TrafficDataClient;
import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusLine;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopArea;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.model.StopName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BusLinesServiceTest {

  private final TrafficDataClient trafficDataClient = mock(TrafficDataClient.class);

  private final TrafficDataRepository trafficDataRepository = mock(TrafficDataRepository.class);

  private final BusLinesService busLinesService = new BusLinesService(trafficDataClient, trafficDataRepository);

  @Test
  void should_fetch_and_store_data() {
    List<BusJourney> busLines = List.of(new BusJourney(new BusNumber(1), List.of(new StopId(10001), new StopId(10002))));
    List<BusStop> busStops = List.of(
            new BusStop(new StopId(10001), new StopArea(10001), new StopName("Nockeby")),
            new BusStop(new StopId(10002), new StopArea(10001), new StopName("Brommaplan")));
    when(trafficDataClient.fetchBusStops()).thenReturn(Mono.just(busStops));
    when(trafficDataClient.fetchBusJourneys()).thenReturn(Mono.just(busLines));

    busLinesService.fetchAndStoreJourneyData();

    verify(trafficDataClient).fetchBusStops();
    verify(trafficDataClient).fetchBusJourneys();
    verify(trafficDataRepository).storeBusStopData(busStops);
    verify(trafficDataRepository).storeBusJourneyData(busLines);
  }

  @Test
  void should_return_bus_lines_with_n_most_stops() {
    List<BusJourney> busJourneys = List.of(
            new BusJourney(new BusNumber(1), List.of(new StopId(10001), new StopId(10002)))
    );
    when(trafficDataRepository.getBusJourneys())
            .thenReturn(busJourneys);
    when(trafficDataRepository.getBusStop(new StopId(10001)))
            .thenReturn(new BusStop(new StopId(10001), new StopArea(10001), new StopName("Nockeby")));
    when(trafficDataRepository.getBusStop(new StopId(10002)))
            .thenReturn(new BusStop(new StopId(10001), new StopArea(10001), new StopName("Brommaplan")));

    List<BusLine> longestBusLines = busLinesService.getBusLinesWithMostStops(10);

    verify(trafficDataRepository).getBusJourneys();
    verify(trafficDataRepository).getBusStops(any());
    assertThat(longestBusLines).hasSize(1);
  }
}
