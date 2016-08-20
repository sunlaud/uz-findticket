package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;

import java.util.List;

@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "error", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ErrorSearchResponse.class, name = "true"),
        @JsonSubTypes.Type(value = SuccessFindTrainResponse.class, name = "null"),
})
public abstract class FindTrainResponse extends SearchResponse<List<TrainDto>> {
}
