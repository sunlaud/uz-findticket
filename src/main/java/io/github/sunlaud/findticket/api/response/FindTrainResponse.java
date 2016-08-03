package io.github.sunlaud.findticket.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.api.model.Train;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FindTrainResponse extends ApiResponse<List<Train>> {
    @JsonProperty("value")
    private List<Train> trains;

    @Override
    public List<Train> getValue() {
        return trains;
    }
}
