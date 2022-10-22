package com.cem.buslines.adapter.rest.outgoing;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cem.buslines.utils.TestUtils.OBJECT_MAPPER;
import static com.cem.buslines.utils.TestUtils.readResource;
import static org.assertj.core.api.Assertions.assertThat;

class TrafficDataResponseTest {

  @Test
  void should_deserialize_journey_response() throws IOException {
    String responseJson = readResource("/responses/get_journeys_response.json");
    ResultData expected = ResultData.builder()
            .lineNumber(1)
            .directionCode(1)
            .journeyPointNumber(10008)
            .lastModifiedDate("2022-02-15 00:00:00.000")
            .existsFromDate("2022-02-15 00:00:00.000")
            .build();

    TrafficDataResponse actual = OBJECT_MAPPER.readValue(responseJson, TrafficDataResponse.class);

    assertThat(actual).isNotNull();
    assertThat(actual.getResponseData().getResult())
            .hasSize(5)
            .first().isEqualTo(expected);
  }

  @Test
  void should_deserialize_stops_response() throws IOException {
    String responseJson = readResource("/responses/get_stops_response.json");
    ResultData expected = ResultData.builder()
            .stopPoint(10001)
            .stopName("Stadshagsplan")
            .stopType("BUSTERM")
            .stopAreaId(10001)
            .lastModifiedDate("2022-02-26 00:00:00.000")
            .existsFromDate("2022-02-26 00:00:00.000")
            .build();

    TrafficDataResponse actual = OBJECT_MAPPER.readValue(responseJson, TrafficDataResponse.class);

    assertThat(actual).isNotNull();
    assertThat(actual.getResponseData().getResult())
            .hasSize(3)
            .first().isEqualTo(expected);
  }
}
