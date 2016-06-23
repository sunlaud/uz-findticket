package io.github.sunlaud.findticket.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class FindTrainRequest {
    @JsonProperty("station_id_from")
    private final Integer stationIdFrom;

    @JsonProperty("station_id_till")
    private final Integer stationIdTill;

    @JsonProperty("time_dep")
    private final String departureTime; //format 22:00

    @JsonProperty("date_dep")
    private final String departureDate; //format 24.06.2016

    public FindTrainRequestBuilder withSwappedStations() {
        return builder()
                .departureTime(departureTime) //need this coz lombok's toBuilder doesn't work for some reason
                .departureDate(departureDate)
                .stationIdTill(stationIdFrom)
                .stationIdFrom(stationIdTill);
    }

//    more params with data from browser client - seems optional:
//    ("another_ec", "0")
//    ("search", "")
//    ("time_dep_till", "")
//    ("station_from", "Дніпропетровськ-Голов.")
//    ("station_till", "Київ")
}
