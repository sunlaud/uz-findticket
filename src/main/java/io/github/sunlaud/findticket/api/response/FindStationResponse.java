package io.github.sunlaud.findticket.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.api.model.Station;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindStationResponse extends ApiResponse<List<Station>> {
    @JsonProperty("value")
    private List<Station> stations;

    @Override
    public List<Station> getValue() {
        return stations;
    }
}
