package io.github.sunlaud.findticket.client.uz;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.recording.RecordingStatus;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static io.github.sunlaud.findticket.filtering.Filters.routeIdEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UzTrainRouteSearchServiceIntegrationTest {
    //also can be used:
    // - https://github.com/square/okhttp/tree/master/mockwebserver
    // - http://www.mock-server.com
    // - https://github.com/jadler-mocking/jadler/wiki
    private WireMockServer mockServer;

    @Before
    public void setup() {
        Options options = WireMockConfiguration.options().bindAddress("127.0.0.1");
        mockServer = new WireMockServer(options);
        mockServer.start();
//      mockServer.startRecording(Apis.BASE_UR);
    }

    @After
    public void shutdown() {
        if (mockServer.getRecordingStatus().getStatus() == RecordingStatus.Recording) {
            mockServer.stopRecording();
        }
        mockServer.stop();
    }

    @Test
    public void findStations() throws Exception {
        //GIVEN
        UzTrainRouteSearchService sut = new UzTrainRouteSearchService(getMockBaseUrl());

        //WHEN
        Station from = sut.findStations("Львів").get(0);
        Station to = sut.findStations("Запоріжжя 1").get(0);
        Collection<TransportRoute> actualRoutes = sut.findRoutes(from, to, LocalDateTime.parse("2017-09-09T00:00"));
        TransportRoute actualRoute = Iterables.getOnlyElement(Iterables.filter(actualRoutes, routeIdEqualTo("120Л")));

        //THEN
        TransportRoute expectedRoute = new TransportRoute();
        expectedRoute.setId("120Л");
        expectedRoute.setName("Львів - Запоріжжя 1");
        expectedRoute.setFrom(new Station("Львів", "2218000"));
        expectedRoute.setTill(new Station("Запоріжжя 1", "2210800"));
        expectedRoute.setDepartureDate(LocalDateTime.parse("2017-09-09T14:45:00.000"));
        expectedRoute.setArrivalDate(LocalDateTime.parse("2017-09-10T15:06:00.000"));
        expectedRoute.setTravelTime(new Period("PT24H21M"));
        HashMap<SeatType, Integer> expectedFreeSeats = new HashMap<>();
        expectedFreeSeats.put(new SeatType("К"), 9);
        expectedFreeSeats.put(new SeatType("Л"), 16);
        expectedFreeSeats.put(new SeatType("П"), 42);
        expectedRoute.setFreeSeatsCountByType(expectedFreeSeats);

        assertThat(actualRoute, equalTo(expectedRoute));
    }


    private String getMockBaseUrl() {
        return String.format("http://%s:%s", mockServer.getOptions().bindAddress(), mockServer.port());
    }

}