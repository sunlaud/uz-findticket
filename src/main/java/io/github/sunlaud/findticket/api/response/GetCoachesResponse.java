package io.github.sunlaud.findticket.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.api.dto.CoachDto;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCoachesResponse {
    @JsonProperty("coach_type_id")
    private int coachTypeId;

    @JsonProperty("coaches")
    private List<CoachDto> coaches;

    @JsonProperty("places_allowed")
    private int placesAllowed;

    @JsonProperty("places_max")
    private int placesMax;

    @JsonProperty("content")
    private String content;
}
