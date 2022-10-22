package com.cem.buslines.domain.model;

import java.util.List;

public record BusLine(BusNumber busNumber, List<BusStop> stops) {
}
