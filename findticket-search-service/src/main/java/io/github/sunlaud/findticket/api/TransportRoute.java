package io.github.sunlaud.findticket.api;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Train/airline/bus route from station A to station B
 */
@Data
public class TransportRoute {
    private String id;
    private String name;

    private Station from;
    private Station till;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

    private Duration travelTime;
    private List<SeatsSummary> freeSeats;
}
