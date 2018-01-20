package io.github.sunlaud.findticket.client.uz;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.recording.RecordingStatus;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.model.JUnitSoftAssertions;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static io.github.sunlaud.findticket.filtering.Filters.routeIdEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class UzTrainRouteSearchServiceIntegrationTest {
    //also can be used:
    // - https://github.com/square/okhttp/tree/master/mockwebserver
    // - http://www.mock-server.com
    // - https://github.com/jadler-mocking/jadler/wiki
    private WireMockServer mockServer;
    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Before
    public void setup() {
        Options options = WireMockConfiguration.options().bindAddress("127.0.0.1");
        mockServer = new WireMockServer(options);
        mockServer.start();
//        mockServer.startRecording(Apis.BASE_URL);
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
        Collection<TransportRoute> actualRoutes = sut.findRoutes(from, to, LocalDateTime.parse("2018-01-23T00:00"));
        TransportRoute actualRoute = Iterables.getOnlyElement(Iterables.filter(actualRoutes, routeIdEqualTo("120Л")));

        //THEN
        TransportRoute expectedRoute = new TransportRoute();
        expectedRoute.setId("120Л");
        expectedRoute.setName("Львів - Запоріжжя 1");
        expectedRoute.setFrom(new Station("Львів", "2218000"));
        expectedRoute.setTill(new Station("Запоріжжя 1", "2210800"));
        expectedRoute.setDepartureDate(LocalDateTime.parse("2018-01-23T14:05:00.000"));
        expectedRoute.setArrivalDate(LocalDateTime.parse("2018-01-24T14:52:00.000"));
        expectedRoute.setTravelTime(new Period("PT24H47M"));
        HashMap<SeatType, Integer> expectedFreeSeats = new HashMap<>();
        expectedFreeSeats.put(new SeatType("К"), 11);
        expectedFreeSeats.put(new SeatType("П"), 139);
        expectedRoute.setFreeSeatsCountByType(expectedFreeSeats);

        assertThat(actualRoute).isEqualTo(expectedRoute);
    }

    @Ignore("Uses real service - can lead to inconsistent test results and build slowdown due to network delays")
    @Test
    public void findStationsViaRealServer() throws Exception {
        //GIVEN
        UzTrainRouteSearchService sut = new UzTrainRouteSearchService();
        final LocalDateTime departure = LocalDate.now().plusDays(29).toDateTimeAtStartOfDay().toLocalDateTime();

        //WHEN
        final Station from = sut.findStations("Львів").get(0);
        final Station to = sut.findStations("Запоріжжя 1").get(0);
        Collection<TransportRoute> actualRoutes = sut.findRoutes(from, to, departure);

        //THEN
        assertThat(actualRoutes).isNotEmpty();
        for (TransportRoute actualRoute : actualRoutes) {
            softly.assertThat(actualRoute).hasFrom(from).hasTill(to);
            softly.assertThat(actualRoute.getDepartureDate()).isGreaterThanOrEqualTo(departure);
            softly.assertThat(actualRoute).hasNoNullFieldsOrProperties();
        }
    }

    private String getMockBaseUrl() {
        return String.format("http://%s:%s", mockServer.getOptions().bindAddress(), mockServer.port());
    }

}