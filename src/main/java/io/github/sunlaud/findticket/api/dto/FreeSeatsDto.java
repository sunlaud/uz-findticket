package io.github.sunlaud.findticket.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FreeSeatsDto {
    @JsonProperty("letter")
    private String letter;

    @JsonProperty("places")
    private Integer places;

    @JsonProperty("title")
    private String title;
}
