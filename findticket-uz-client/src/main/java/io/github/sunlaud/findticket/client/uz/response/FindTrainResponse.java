package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindTrainResponse extends SearchResponse<List<TrainDto>> {
    @JsonProperty("data")
    private UglyValueWrapper valueWrapper;

    @Override
    public List<TrainDto> getValue() {
        return valueWrapper.value;
    }

    private static class UglyValueWrapper {
        @JsonProperty("list")
        private List<TrainDto> value;
    }
}
