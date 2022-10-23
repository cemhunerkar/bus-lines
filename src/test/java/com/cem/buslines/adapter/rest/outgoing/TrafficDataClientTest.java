package com.cem.buslines.adapter.rest.outgoing;

import com.cem.buslines.configuration.ExternalClientException;
import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopId;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.cem.buslines.utils.TestUtils.readResource;
import static com.cem.buslines.utils.TestUtils.testClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.contract.spec.internal.MediaTypes.APPLICATION_JSON;

class TrafficDataClientTest {

  private static final String API_KEY = "791e329cb73847399d40229a7ca09a28";

  private final WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.DYNAMIC_PORT);

  @BeforeEach
  void setUp() {
    wireMockServer.start();
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }

  @Test
  void should_get_journey_details() throws IOException {
    WebClient webClientForTest = testClient(wireMockServer.baseUrl());
    TrafficDataClient trafficDataClient = new TrafficDataClient(webClientForTest, API_KEY);
    String mockResponse = readResource("/responses/get_journeys_response.json");
    stubTrafficResponse(mockResponse, "jour");

    Mono<List<BusJourney>> actual = trafficDataClient.fetchBusJourneys();

    StepVerifier
            .create(actual)
            .assertNext(busJourneys -> {
              assertThat(busJourneys).hasSize(1);
              assertThat(busJourneys.get(0).stops())
                      .hasSize(5)
                      .contains(new StopId(10008));
            })
            .verifyComplete();
  }

  @Test
  void should_throw_when_external_journey_details_call_fails() {
    WebClient webClientForTest = testClient(wireMockServer.baseUrl());
    TrafficDataClient trafficDataClient = new TrafficDataClient(webClientForTest, API_KEY);
    wireMockServer.stubFor(
            get(urlPathEqualTo("/api2/LineData.json"))
                    .willReturn(aResponse()
                            .withStatus(400)));

    Mono<List<BusJourney>> actual = trafficDataClient.fetchBusJourneys();

    StepVerifier
            .create(actual)
            .expectErrorSatisfies(exception ->
                    assertThat(exception)
                            .isInstanceOf(ExternalClientException.class)
                            .hasMessage("Problem occurred in external call"))
            .verify();
  }

  @Test
  void should_get_stop_details() throws IOException {
    WebClient webClientForTest = testClient(wireMockServer.baseUrl());
    TrafficDataClient trafficDataClient = new TrafficDataClient(webClientForTest, API_KEY);
    String mockResponse = readResource("/responses/get_stops_response.json");
    stubTrafficResponse(mockResponse, "stop");

    Mono<List<BusStop>> actual = trafficDataClient.fetchBusStops();

    StepVerifier
            .create(actual)
            .assertNext(busStops ->
                    assertThat(busStops)
                            .hasSize(3)
                            .extracting(BusStop::stopId)
                            .containsOnly(new StopId(10001), new StopId(10002), new StopId(10003)))
            .verifyComplete();
  }

  @Test
  void should_throw_when_external_stop_details_call_fails() {
    WebClient webClientForTest = testClient(wireMockServer.baseUrl());
    TrafficDataClient trafficDataClient = new TrafficDataClient(webClientForTest, API_KEY);
    wireMockServer.stubFor(
            get(urlPathEqualTo("/api2/LineData.json"))
                    .willReturn(aResponse()
                            .withStatus(400)));

    Mono<List<BusStop>> actual = trafficDataClient.fetchBusStops();

    StepVerifier
            .create(actual)
            .expectErrorSatisfies(exception ->
                    assertThat(exception)
                            .isInstanceOf(ExternalClientException.class)
                            .hasMessage("Problem occurred in external call"))
            .verify();
  }

  private void stubTrafficResponse(String body, String model) {
    wireMockServer.stubFor(
            get(urlPathEqualTo("/api2/LineData.json"))
                    .withQueryParam("model", equalTo(model))
                    .withQueryParam("key", equalTo(API_KEY))
                    .willReturn(aResponse()
                            .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                            .withBody(body)));

  }
}
