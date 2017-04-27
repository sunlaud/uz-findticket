package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalTimeSerializer;
import lombok.Data;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalTime;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainDto {
    private static final String TIME_FORMAT = "HH:mm";

    @JsonProperty("num")
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    @JsonProperty("travel_time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime travelTime;

    /** this is actually the train start station (may be not the station in search request) */
    @JsonProperty("from")
    private TimeAndPlaceDto from;

    /** this is actually the train end station (may be not the station in search request) */
    @JsonProperty("till")
    private TimeAndPlaceDto till;

    @JsonProperty("types")
    private List<FreeSeatsDto> freeSeats;

    public Duration getTravelTime() {
        return Duration.standardSeconds(travelTime.get(DateTimeFieldType.secondOfDay()));
    }

    public String getName() {
        return from.getStationName() + " - " + till.getStationName();
    }
}
