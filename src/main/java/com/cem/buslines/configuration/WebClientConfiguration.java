package com.cem.buslines.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

  private static final int SIXTEEN_MB = 16 * 1024 * 1024;

  @Bean
  public WebClient webClient(@Value("${client.sl.base-url}") String baseUrl) {
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
