package io.github.sunlaud.findticket.api;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Train route from station A to station B
 */
@Data
public class TrainRoute {
    private String trainID;
    private Station fromStation;
    private Station toStation;
    private LocalDateTime arriveAt;
    private LocalDateTime departureAt;
}
