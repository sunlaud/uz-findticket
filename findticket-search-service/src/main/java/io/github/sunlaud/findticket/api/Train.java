package io.github.sunlaud.findticket.api;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Train {
    private String number;

    private Station stationFrom;
    private Station stationTill;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;

    private Duration travelTime;
    private List<SeatsSummary> freeSeats;
}
