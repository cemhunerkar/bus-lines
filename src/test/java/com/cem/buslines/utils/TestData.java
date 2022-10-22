package com.cem.buslines.utils;

import com.cem.buslines.domain.model.BusJourney;
import com.cem.buslines.domain.model.BusLine;
import com.cem.buslines.domain.model.BusNumber;
import com.cem.buslines.domain.model.BusStop;
import com.cem.buslines.domain.model.StopArea;
import com.cem.buslines.domain.model.StopId;
import com.cem.buslines.domain.model.StopName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestData {

  public static List<BusLine> longestBusLines(int numberOfResults) {
    Comparator<BusJourney> reverseOrderComparator = (b1, b2) -> Integer.compare(b2.stops().size(), b1.stops().size());
    return testBusJourneys().stream()
            .sorted(reverseOrderComparator)
            .map(line -> new BusLine(line.busNumber(), testBusStops(line.stops().size())))
            .limit(numberOfResults)
            .toList();
  }

  public static List<BusJourney> testBusJourneys() {
    return List.of(
            new BusJourney(new BusNumber(1), testBusStops(15).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(2), testBusStops(2).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(3), testBusStops(4).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(4), testBusStops(5).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(5), testBusStops(9).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(6), testBusStops(6).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(7), testBusStops(6).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(8), testBusStops(7).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(9), testBusStops(3).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(10), testBusStops(1).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(11), testBusStops(8).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(12), testBusStops(4).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(13), testBusStops(12).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(14), testBusStops(13).stream().map(BusStop::stopId).toList()),
            new BusJourney(new BusNumber(15), testBusStops(3).stream().map(BusStop::stopId).toList())
    );
  }

  public static List<BusStop> testBusStops(int count) {
    return testBusStops().stream().limit(count).toList();
  }

  public static List<BusStop> testBusStops() {
    List<BusStop> busStops = new ArrayList<>();
    busStops.add(new BusStop(new StopId(10001), new StopArea(10001), new StopName("Stadshagsplan")));
    busStops.add(new BusStop(new StopId(10002), new StopArea(10002), new StopName("John Bergs plan")));
    busStops.add(new BusStop(new StopId(10006), new StopArea(10006), new StopName("Arbetargatan")));
    busStops.add(new BusStop(new StopId(10010), new StopArea(10010), new StopName("Frihamnens färjeterminal")));
    busStops.add(new BusStop(new StopId(10011), new StopArea(10011), new StopName("Frihamnsporten")));
    busStops.add(new BusStop(new StopId(10012), new StopArea(10012), new StopName("Celsiusgatan")));
    busStops.add(new BusStop(new StopId(10013), new StopArea(10013), new StopName("S:t Eriksgatan")));
    busStops.add(new BusStop(new StopId(10014), new StopArea(10014), new StopName("Scheelegatan")));
    busStops.add(new BusStop(new StopId(10016), new StopArea(10016), new StopName("Kungsbroplan")));
    busStops.add(new BusStop(new StopId(10018), new StopArea(10018), new StopName("Stadshagsplan")));
    busStops.add(new BusStop(new StopId(10019), new StopArea(10019), new StopName("Cityterminalen")));
    busStops.add(new BusStop(new StopId(10020), new StopArea(10020), new StopName("Vasagatan")));
    busStops.add(new BusStop(new StopId(10022), new StopArea(10022), new StopName("Ruddammen")));
    busStops.add(new BusStop(new StopId(10024), new StopArea(10024), new StopName("Hötorget")));
    busStops.add(new BusStop(new StopId(10033), new StopArea(10033), new StopName("Linnégatan")));
    busStops.add(new BusStop(new StopId(10034), new StopArea(10034), new StopName("Humlegården")));
    busStops.add(new BusStop(new StopId(10036), new StopArea(10036), new StopName("Ruddammsparken")));
    busStops.add(new BusStop(new StopId(10037), new StopArea(10037), new StopName("Nybrogatan")));
    busStops.add(new BusStop(new StopId(10039), new StopArea(10039), new StopName("Ruddammen")));
    busStops.add(new BusStop(new StopId(10042), new StopArea(10042), new StopName("Jungfrugatan")));
    busStops.add(new BusStop(new StopId(10044), new StopArea(10044), new StopName("Värtavägen")));

    return busStops;
  }
}
