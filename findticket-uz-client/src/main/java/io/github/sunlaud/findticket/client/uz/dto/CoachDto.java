package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoachDto {
    @JsonProperty("num")
    private int number;

    /** sample values Л, К, П */
    @JsonProperty("type")
    private String type;

    @JsonProperty("places_cnt")
    private int placesCount;

    @JsonProperty("coach_type_id")
    private int coachTypeId;

    /** sample values А, Б */
    @JsonProperty("coach_class")
    private String coachClass;

    /** sample values { A: 1253 } */
    @JsonProperty("prices")
    private Map<String, Integer> prices;
}
