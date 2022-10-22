package com.cem.buslines.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;

public class TestUtils {

  private static final int SIXTEEN_MB = 16 * 1024 * 1024;

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

  public static String readResource(String path) throws IOException {
    ClassPathResource classPathResource = new ClassPathResource(path);
    return IOUtils.toString(classPathResource.getInputStream(), "UTF-8");
  }

  public static WebClient testClient(String baseUrl) {
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(SIXTEEN_MB))
            .build();
    return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate")
            .exchangeStrategies(strategies)
            .build();
  }
}
