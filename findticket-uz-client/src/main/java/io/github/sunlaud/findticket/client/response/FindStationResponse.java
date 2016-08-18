package io.github.sunlaud.findticket.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.dto.StationDto;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindStationResponse extends ApiResponse<List<StationDto>> {
    @JsonProperty("value")
    private List<StationDto> stations;

    @Override
    public List<StationDto> getValue() {
        return stations;
    }
}
