package io.github.sunlaud.findticket.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Station {
    @JsonProperty("title")
    private String title;

    @JsonProperty("station_id")
    private int id;
}
