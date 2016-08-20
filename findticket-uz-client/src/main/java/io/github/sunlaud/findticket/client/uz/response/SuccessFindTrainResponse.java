package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessFindTrainResponse extends FindTrainResponse {
    @JsonProperty("value")
    private List<TrainDto> trains;

    @Override
    public List<TrainDto> getValue() {
        return trains;
    }
}
