package io.github.sunlaud.findticket.client.uz.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class GetCoachesRequest {
    private static final DateTimeZone TIMEZONE = DateTimeZone.forID("Europe/Kiev");

    @JsonProperty("station_id_from")
    private final int stationIdFrom;

    @JsonProperty("station_id_till")
    private final int stationIdTill;

    @JsonProperty("train")
    private final String trainNumber; //063Д

    @JsonProperty("coach_type")
    private final String coachType; //К, П, Л

    @JsonIgnore
    private final LocalDateTime departureDate;

    @JsonIgnore
    private final long departureDateAsEpochSecond;

    @JsonProperty("date_dep") //format 1473351000 (for 08.09.2016)
    private long getDepartureDateAsEpochSecond() {
        return departureDate != null
                ? departureDate.toDateTime(TIMEZONE).toInstant().getMillis()
                : departureDateAsEpochSecond;
    }

//    model=0
//    round_trip=0
//    another_ec=0
}
