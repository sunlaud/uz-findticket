package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessFindStationResponse extends FindStationResponse {
    @JsonProperty("value")
    private List<StationDto> stations;

    @Override
    public List<StationDto> getValue() {
        return stations;
    }
}
