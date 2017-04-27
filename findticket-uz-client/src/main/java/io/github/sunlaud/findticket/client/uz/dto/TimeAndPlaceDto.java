package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTimeConstants;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeAndPlaceDto {
    /** this is actually the train start/end station (may be not the station in search request) */
    @JsonProperty("station")
    private String stationName;

    /** time when train arrives/departures to/from station in search request (may not be the same as {@link #stationName} */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("src_date")
    private LocalDateTime date;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JsonProperty("date")
    private long dateAsEpochSecond;

    private Instant getDateAsInstant() {
        return new Instant(dateAsEpochSecond * DateTimeConstants.MILLIS_PER_SECOND);
    }

    @Override
    public String toString() {
        return stationName + " (" + date + ")";
    }
}
