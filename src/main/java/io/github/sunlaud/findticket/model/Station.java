package io.github.sunlaud.findticket.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Station {
    private String title;
    @JsonProperty("station_id")
    private int id;
}
