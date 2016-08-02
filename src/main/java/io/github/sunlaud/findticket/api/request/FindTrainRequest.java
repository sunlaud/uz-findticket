package io.github.sunlaud.findticket.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder(toBuilder = true)
public class FindTrainRequest {
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);


    @JsonProperty("station_id_from")
    private final Integer stationIdFrom;

    @JsonProperty("station_id_till")
    private final Integer stationIdTill;

    @JsonProperty("time_dep")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private final LocalTime departureTime; //format 22:00

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("date_dep")
    private final LocalDate departureDate; //format 24.06.2016

//    @JsonProperty("date_dep")
//    private final String departureDate; //format 24.06.2016

    public FindTrainRequestBuilder withSwappedStations() {
        return builder()
                .departureTime(departureTime) //need this coz lombok's toBuilder doesn't work for some reason
                .departureDate(departureDate)
                .stationIdTill(stationIdFrom)
                .stationIdFrom(stationIdTill);
    }

    public FindTrainRequestBuilder withDate(LocalDate newDepartureDate) {
        return builder()
                .departureTime(departureTime) //need this coz lombok's toBuilder doesn't work for some reason
                .departureDate(newDepartureDate)
                .stationIdTill(stationIdTill)
                .stationIdFrom(stationIdFrom);
    }

//    more params with data from browser client - seems optional:
//    ("another_ec", "0")
//    ("search", "")
//    ("time_dep_till", "")
//    ("station_from", "Дніпропетровськ-Голов.")
//    ("station_till", "Київ")
}
