package io.github.sunlaud.findticket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.net.URL;
import java.util.Map;

/**
 * Train/airline/bus route from station A to station B
 */
@EqualsAndHashCode(exclude = { "detailsUrl" })
@Data
public class TransportRoute {
    private String id;
    private String name;

    private Station from;
    private Station to;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

    private Period travelTime;
    private Map<SeatType, Integer> freeSeatsCountByType;
    private URL detailsUrl;
}
