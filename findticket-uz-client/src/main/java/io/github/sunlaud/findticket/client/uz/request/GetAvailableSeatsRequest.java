package io.github.sunlaud.findticket.client.uz.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder(toBuilder = true)
public class GetAvailableSeatsRequest {
    private static final ZoneId TIMEZONE = ZoneId.of("Europe/Kiev");

    @JsonProperty("station_id_from")
    private final int stationIdFrom;

    @JsonProperty("station_id_till")
    private final int stationIdTill;

    @JsonProperty("train")
    private final String trainNumber; //063Д

    @JsonProperty("coach_class")
    private final String coachClass; //А, Б

    @JsonProperty("coach_num")
    private final int coachNumber;

    @JsonProperty("coach_type_id")
    private final int coachTypeId; //3

    @JsonIgnore
    private final LocalDateTime departureDate;

    @JsonIgnore
    private final long departureDateAsEpochSecond;

    @JsonProperty("date_dep") //format 1473351000 (for 08.09.2016)
    private long getDepartureDateAsEpochSecond() {
        return departureDate != null
                ? departureDate.atZone(TIMEZONE).toInstant().getEpochSecond()
                : departureDateAsEpochSecond;
    }

    //change_scheme=0
}
