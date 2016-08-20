package io.github.sunlaud.findticket.api;

import lombok.Data;

@Data
public class SeatsSummary {
    private String letter;
    private int places;
    private String title;
}
