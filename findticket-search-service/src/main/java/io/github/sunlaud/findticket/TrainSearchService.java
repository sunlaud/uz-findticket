package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.TrainRoute;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

public interface TrainSearchService {
    /**
     * Available routes to station at given day.
     * @param to to station
     * @param at at time
     * @return available routes or empty iterable
     */
    Iterable<TrainRoute> availableRoutes(Station to, LocalDate at);
}
