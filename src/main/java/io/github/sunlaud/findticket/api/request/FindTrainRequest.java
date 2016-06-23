package io.github.sunlaud.findticket.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindTrainRequest {
    @JsonProperty("station_id_from")
    private final Integer stationIdFrom;

    @JsonProperty("station_id_till")
    private final Integer stationIdTill;

    @JsonProperty("time_dep")
    private final String departureTime; //format 22:00

    @JsonProperty("date_dep")
    private final String departureDate; //format 24.06.2016

//    more params with data from browser client - seems optional:
//    ("another_ec", "0")
//    ("search", "")
//    ("time_dep_till", "")
//    ("station_from", "Дніпропетровськ-Голов.")
//    ("station_till", "Київ")
}
