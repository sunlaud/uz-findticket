package io.github.sunlaud.findticket.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.api.model.Coach;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCoachesResponse {
    @JsonProperty("coach_type_id")
    private Integer coachTypeId;

    @JsonProperty("coaches")
    private List<Coach> coaches;
}
