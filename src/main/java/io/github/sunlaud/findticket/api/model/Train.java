package io.github.sunlaud.findticket.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    @JsonProperty("num")
    private String number;

    @JsonProperty("travel_time")
    private String travelTime;

    @JsonProperty("from")
    private TimeAndPlace from;

    @JsonProperty("till")
    private TimeAndPlace till;

    @JsonProperty("types")
    private List<FreeSeatsSummary> freeSeats;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("number", number)
                .add("from", from)
                .add("till", till)
                .add("travelTime", travelTime)
                .toString();
    }
}
