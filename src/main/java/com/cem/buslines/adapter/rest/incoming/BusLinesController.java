package com.cem.buslines.adapter.rest.incoming;

import com.cem.buslines.configuration.ValidationException;
import com.cem.buslines.domain.ports.BusLinesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@ResponseBody
@RestController
public class BusLinesController {

  private final BusLinesService busLinesService;

  public BusLinesController(BusLinesService busLinesService) {
    this.busLinesService = busLinesService;
  }

  @GetMapping("/topLines")
  public Flux<BusLineResponse> getTopLines(@RequestParam Integer numberOfResults) {
    log.debug("Received getTopLines request: {}", numberOfResults);
    validate(numberOfResults);

    return Flux.fromIterable(busLinesService.getBusLinesWithMostStops(numberOfResults))
            .map(BusLineResponse::fromDomain);
  }

  @GetMapping("/refresh")
  public void refreshData() {
    log.debug("Received refresh data request");
    busLinesService.fetchAndStoreJourneyData();
  }

  private void validate(Integer numberOfResults) {
    if (numberOfResults == null || numberOfResults <= 0 || numberOfResults > 50) {
      throw new ValidationException("Number of results should be in range 1-50");
    }
  }
}
