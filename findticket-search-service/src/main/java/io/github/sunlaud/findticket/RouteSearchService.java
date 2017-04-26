package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.TransportRoute;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteSearchService {
    /**
     * Available routes to station at given date.
     */
    Iterable<TransportRoute> findRoutes(Station stationFrom, Station stationTo, LocalDateTime departureDate);

    List<Station> findStations(String nameSubstring);
}
