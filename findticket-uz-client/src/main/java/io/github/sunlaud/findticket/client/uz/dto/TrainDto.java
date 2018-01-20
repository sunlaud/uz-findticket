package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.PeriodSerializer;
import io.github.sunlaud.findticket.client.uz.response.deserialize.CustomFormatPeriodDeserializer;
import lombok.Data;
import org.joda.time.Period;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainDto {
    private static final String TIME_FORMAT = "HH:mm";

    @JsonProperty("num")
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm")
    @JsonProperty("travelTime")
    @JsonDeserialize(using = CustomFormatPeriodDeserializer.class)
    @JsonSerialize(using = PeriodSerializer.class)
    private Period travelTime;

    /** this is actually the train start station (may be not the station in search request) */
    @JsonProperty("from")
    private TimeAndPlaceDto from;

    /** this is actually the train end station (may be not the station in search request) */
    @JsonProperty("to")
    private TimeAndPlaceDto till;

    @JsonProperty("types")
    private List<FreeSeatsDto> freeSeats;

    public String getName() {
        return from.getStationName() + " - " + till.getStationName();
    }
}
