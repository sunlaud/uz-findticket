package io.github.sunlaud.findticket.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FreeSeatsSummary {
    @JsonProperty("title")
    private String title;

    @JsonProperty("letter")
    private String letter;

    @JsonProperty("places")
    private Integer places;
}
