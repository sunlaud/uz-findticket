package io.github.sunlaud.findticket.model;

import lombok.Data;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import java.util.Map;

/**
 * Train/airline/bus route from station A to station B
 */
@Data
public class TransportRoute {
    private String id;
    private String name;

    private Station from;
    private Station to;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

    //TODO - should be Duration
    private Duration travelTime;
    private Map<SeatType, Integer> freeSeatsCountByType;
}
