package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import lombok.Data;

import java.util.List;

//TODO check and remove: looks like this class is not needed any more (due to UZ API change)
@Deprecated
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindStationResponse extends SearchResponse<List<StationDto>> {
    @JsonProperty("value")
    private List<StationDto> value;
}

