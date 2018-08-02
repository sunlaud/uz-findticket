package io.github.sunlaud.findticket.client.uz.dto;

import io.github.sunlaud.findticket.client.uz.dto.TrainDto.TrainDetailsUrlBuilder;
import lombok.SneakyThrows;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainDetailsUrlBuilderTest {

    @SneakyThrows
    @Test
    public void buildsDetailsUrl() {
        //GIVEN
        TimeAndPlaceDto from = new TimeAndPlaceDto();
        from.setStationIdActual(2200001);
        from.setDateNoTime("середа, 15.08.2018");
        from.setTime(LocalTime.MIDNIGHT);

        TimeAndPlaceDto to = new TimeAndPlaceDto();
        to.setStationIdActual(2208001);

        TrainDto train = new TrainDto();
        train.setId("223К");
        train.setFrom(from);
        train.setTill(to);


        //WHEN
        URL actualUrl = TrainDetailsUrlBuilder.buildDetailsUrl(train);

        //THEN
        assertThat(actualUrl).isEqualTo(URI.create("https://booking.uz.gov.ua/?from=2200001&to=2208001&date=2018-08-15&train=223К&url=train-wagons").toURL());
    }
}