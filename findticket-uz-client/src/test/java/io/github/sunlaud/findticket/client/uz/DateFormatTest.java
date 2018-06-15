package io.github.sunlaud.findticket.client.uz;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DateFormatTest {

    public static class Dto {
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd.MM.yyyy", locale = "uk")
        @JsonProperty("date")
        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }
    }

    //this test passes on desktop computer, but deserialization fails on android for some reason
    @Test
    public void objectMapperIsAbleToParseUkrainianDayOfWeek() throws IOException {
        String json = "{ \"date\": \"п'ятниця, 01.06.2018\" }";

        Dto dto = new ObjectMapper().readValue(json, Dto.class);

        assertEquals(new LocalDate(2018, 6, 1), dto.getDate());
    }
}
