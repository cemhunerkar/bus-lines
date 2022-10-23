package com.cem.buslines;

import com.cem.buslines.domain.ports.BusLinesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class BusLinesApplication {

  private final BusLinesService busLinesService;

  public BusLinesApplication(BusLinesService busLinesService) {
    this.busLinesService = busLinesService;
  }

  public static void main(String[] args) {
    SpringApplication.run(BusLinesApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void fetchTrafficData(ApplicationReadyEvent readyEvent) {
    String[] activeProfiles = readyEvent.getApplicationContext().getEnvironment().getActiveProfiles();

    if (!Arrays.asList(activeProfiles).contains("test")) {
      log.info("Fetching traffic data on startup");
      busLinesService.fetchAndStoreJourneyData();
    }
  }

  /*
   * SL updates the data between 00:00 and 02:00, this scheduler will run at 02:05
   */
  @Scheduled(cron = "${cron.traffic-data}")
  public void refreshTrafficData() {
    log.info("[JOB] Refreshing traffic data");
    busLinesService.fetchAndStoreJourneyData();
  }
}
