package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.joda.time.LocalDateTime;

import java.util.List;

public interface RouteSearchService {
    Iterable<TransportRoute> findRoutes(Station stationFrom, Station stationTo, LocalDateTime departureDate);
    List<Station> findStations(String namePrefix);
}
