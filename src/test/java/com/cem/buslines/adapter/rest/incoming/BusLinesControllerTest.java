package com.cem.buslines.adapter.rest.incoming;

import com.cem.buslines.configuration.ControllerErrorHandler;
import com.cem.buslines.domain.model.BusLine;
import com.cem.buslines.domain.ports.BusLinesService;
import com.cem.buslines.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.List;

import static com.cem.buslines.utils.TestUtils.readResource;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BusLinesControllerTest {

  private WebTestClient webTestClient;

  private final BusLinesService busLinesService = mock(BusLinesService.class);

  @BeforeEach
  void setUp() {
    BusLinesController busLinesController = new BusLinesController(busLinesService);
    webTestClient = WebTestClient
            .bindToController(busLinesController)
            .controllerAdvice(new ControllerErrorHandler())
            .build();
  }

  @Test
  void should_respond_with_n_bus_lines_with_most_stops() throws IOException {
    int numberOfResults = 10;
    String expectedResponse = readResource("/responses/get_top_lines_response.json");
    List<BusLine> longestBusLines = TestData.longestBusLines(numberOfResults);
    when(busLinesService.getBusLinesWithMostStops(numberOfResults)).thenReturn(longestBusLines);

    webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/v1/bus-lines/top")
                    .queryParam("numberOfResults", "10").build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().json(expectedResponse, true);

    verify(busLinesService).getBusLinesWithMostStops(numberOfResults);
  }

  @ParameterizedTest
  @ValueSource(ints = {-10, 0, 51})
  void should_return_bad_request_when_the_parameter_is_invalid(int numberOfResults) {
    String errorResponse = """
            {
               errorCode: 10000,
               errorMessage: "Number of results should be in range 1-50"
            }
             """;

    webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/v1/bus-lines/top")
                    .queryParam("numberOfResults", numberOfResults).build())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().json(errorResponse);
  }

  @Test
  void should_refresh_data() {
    webTestClient.get()
            .uri("/v1/refresh")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
    verify(busLinesService).fetchAndStoreJourneyData();
  }
}
