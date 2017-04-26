package io.github.sunlaud.findticket.api;

import lombok.Data;

@Data
public class SeatsSummary {
    private String id;
    private String name;
    private int places;
}
