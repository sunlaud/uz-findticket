package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StationDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("station_id")
    private int id;
}
