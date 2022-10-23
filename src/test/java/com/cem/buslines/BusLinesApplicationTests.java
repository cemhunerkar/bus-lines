package com.cem.buslines;

import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.utils.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;

/*
This will be the E2E test class. There will be no mocks.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class BusLinesApplicationTests {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private Map<BusNumber, BusJourney> busJourneysMap;

  @Autowired
  private Map<StopId, BusStop> busStopsMap;

  @Test
  void should_return_top_ten_longest_bus_lines_successfully() {
    List<BusJourney> busJourneys = TestData.testBusJourneys();
    busJourneys.forEach(journey -> busJourneysMap.put(journey.busNumber(), journey));
    List<BusStop> busStops = TestData.testBusStops();
    busStops.forEach(stop -> busStopsMap.put(stop.stopId(), stop));

    webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/v1/bus-lines/top")
                    .queryParam("numberOfResults", "10").build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.length()").isEqualTo(10)
            //first element
            .jsonPath("$[0].busNumber").isEqualTo(1)
            .jsonPath("$[0].busStops.length()").isEqualTo(15)
            // 10th element
            .jsonPath("$.[-1].busNumber").isEqualTo(3)
            .jsonPath("$.[-1].busStops.length()").isEqualTo(4);
  }
}
