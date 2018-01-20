package io.github.sunlaud.findticket.client.uz.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalTimeSerializer;
import lombok.Builder;
import lombok.Data;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


@Data
@Builder(toBuilder = true)
public class FindTrainRequest {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern(TIME_FORMAT);


    @JsonProperty("from")
    private final int stationIdFrom;

    @JsonProperty("to")
    private final int stationIdTill;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    @JsonProperty("time")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private final LocalTime departureTime; //format 22:00

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("date")
    private final LocalDate departureDate; //format 2018-01-20

    public FindTrainRequest comingBackOn(LocalDateTime dateBack) {
        return toBuilder()
                .stationIdTill(stationIdFrom)
                .stationIdFrom(stationIdTill)
                .departureDate(dateBack.toLocalDate())
                .departureTime(dateBack.toLocalTime())
                .build();
    }

//    more params with data from browser client - seems optional:
//    ("another_ec", "0")
//    ("search", "")
//    ("time_dep_till", "")
//    ("station_from", "Дніпропетровськ-Голов.")
//    ("station_till", "Київ")
}
