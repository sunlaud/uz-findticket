package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StationDto {
    @JsonProperty("label")
    private String title;

    @JsonProperty("value")
    private int id;
}
