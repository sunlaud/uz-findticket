package io.github.sunlaud.findticket.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Train route from station A to station B
 */
@Data
@Builder
public class TrainRoute {
    private String trainID;
    private Station fromStation;
    private Station toStation;
    private LocalDateTime arriveAt;
    private LocalDateTime departureAt;
}
