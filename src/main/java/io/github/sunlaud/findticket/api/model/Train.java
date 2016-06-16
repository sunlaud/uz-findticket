package io.github.sunlaud.findticket.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    @JsonProperty("num")
    private String number;

    @JsonProperty("travel_time")
    private String travelTime;

    @JsonProperty("src_date")
    private TimeAndPlace from;

    @JsonProperty("till")
    private TimeAndPlace till;

    @JsonProperty("types")
    private List<FreeSeatsSummary> freeSeats;
}
