package io.github.sunlaud.findticket.client.uz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.PeriodSerializer;
import io.github.sunlaud.findticket.client.uz.client.Apis;
import io.github.sunlaud.findticket.client.uz.response.deserialize.CustomFormatPeriodDeserializer;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URI;
import java.net.URL;
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

    public URL getDetailsUrl() {
        return TrainDetailsUrlBuilder.buildDetailsUrl(this);
    }

    @UtilityClass
    public static class TrainDetailsUrlBuilder {
        private static final DateTimeFormatter URL_DATE_FORMAT =DateTimeFormat.forPattern("yyyy-MM-dd");

        @SneakyThrows
        public static URL buildDetailsUrl(TrainDto train) {
            String date = URL_DATE_FORMAT.print(train.getFrom().getDate());
            String urlString = String.format("%s/?from=%s&to=%s&date=%s&train=%s&url=train-wagons",
                    Apis.BASE_URL, train.getFrom().getStationIdActual(), train.getTill().getStationIdActual(), date, train.getId());
            return URI.create(urlString).toURL();
        }
    }
}
