package io.github.sunlaud.findticket.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeAndPlace {
    @JsonProperty("station")
    private String station;

    @JsonProperty("src_date")
    private String date;
}
