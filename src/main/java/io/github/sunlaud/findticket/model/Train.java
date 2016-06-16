package io.github.sunlaud.findticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    @JsonProperty("num")
    private String number;
    private String travelTime;
    private TimeAndPlace from;
    private TimeAndPlace till;
    @JsonProperty("types")
    private List<FreeSeatsSummary> freeSeats;
}
