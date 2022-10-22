package com.cem.buslines.domain.model;

import java.util.List;

public record BusJourney(BusNumber busNumber, List<StopId> stops) {
}
