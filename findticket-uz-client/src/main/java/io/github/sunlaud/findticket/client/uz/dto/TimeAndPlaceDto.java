package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalTimeSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTimeConstants;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.Locale;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeAndPlaceDto {
    private static final Locale UKRAINIAN_LOCALE = new Locale("ur", "ua");
    /** this is actually the train start/end station (may be not the station in search request) */
    @JsonProperty("stationTrain")
    private String stationName;

    //TODO refactor to use this
    /** this is an actual target/source station from request (new API) */
    @JsonProperty("station")
    private String stationNameActual;

    //TODO refactor to use this
    /** this is an actual target/source station from request (new API) */
    @JsonProperty("code")
    private int stationIdActual;

    @Getter(AccessLevel.NONE)
    /** date when train arrives/departures to/from station in search request (may not be the same as {@link #stationName} */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd.MM.yyyy", locale = "uk")
    @JsonProperty("date")
    private LocalDate dateNoTime; //e.g. субота, 20.01.2018

    @Getter(AccessLevel.NONE)
    /** time when train arrives/departures to/from station in search request (may not be the same as {@link #stationName} */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonProperty("time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime time; //format 22:00

    /** time when train arrives/departures to/from station in search request (may not be the same as {@link #stationName} */
    public LocalDateTime getDate() {
        return dateNoTime.toLocalDateTime(time);
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JsonProperty("sortTime")
    private long dateAsEpochSecond;

    private Instant getDateAsInstant() {
        return new Instant(dateAsEpochSecond * DateTimeConstants.MILLIS_PER_SECOND);
    }

    @Override
    public String toString() {
        return stationName + " (" + getDate() + ")";
    }
}
