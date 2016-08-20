package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.Train;
import io.github.sunlaud.findticket.api.TrainRoute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TrainSearchService {
    /**
     * Available routes to station at given day.
     * @param toStationId to station
     * @param fromStationId from station
     * @param at at time
     * @return available routes or empty iterable
     */
    Iterable<TrainRoute> availableRoutes(String toStationId, String fromStationId,
                                         LocalDate at);

    List<Station> findStations(String prefix);

    List<Train> findTrains(Station from, Station till, LocalDateTime departure);
}
