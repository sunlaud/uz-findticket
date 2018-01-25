package io.github.sunlaud.findticket;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TripPlannerTest {
    @Test
    public void searchesWithBackwardDaysOffset() {
        //GIVEN
        RouteSearchService routeSearchService = mock(RouteSearchService.class);
        TripPlanner sut = new TripPlanner(routeSearchService);
        DaysOffset offset = new DaysOffset(3, OffsetDirection.BACKWARD);
        LocalDateTime departureDate = LocalDateTime.parse("2010-05-20T12:00:00");

        //WHEN
        sut.findRoutes(null, null, departureDate, offset);

        //THEN
        verify(routeSearchService).findRoutes(null, null, departureDate.minusDays(3));
        verify(routeSearchService).findRoutes(null, null, departureDate.minusDays(2));
        verify(routeSearchService).findRoutes(null, null, departureDate.minusDays(1));
        verify(routeSearchService).findRoutes(null, null, departureDate);
    }

    @Test
    public void searchesWithForwardDaysOffset() {
        //GIVEN
        RouteSearchService routeSearchService = mock(RouteSearchService.class);
        TripPlanner sut = new TripPlanner(routeSearchService);
        DaysOffset offset = new DaysOffset(3, OffsetDirection.FORWARD);
        LocalDateTime departureDate = LocalDateTime.parse("2010-05-20T12:00:00");

        //WHEN
        sut.findRoutes(null, null, departureDate, offset);

        //THEN
        verify(routeSearchService).findRoutes(null, null, departureDate.plusDays(3));
        verify(routeSearchService).findRoutes(null, null, departureDate.plusDays(2));
        verify(routeSearchService).findRoutes(null, null, departureDate.plusDays(1));
        verify(routeSearchService).findRoutes(null, null, departureDate);
    }

    @Test
    public void searchesWithBothForwardAndBackwardsDaysOffset() {
        //GIVEN
        RouteSearchService routeSearchService = mock(RouteSearchService.class);
        TripPlanner sut = new TripPlanner(routeSearchService);
        DaysOffset offset = new DaysOffset(3, OffsetDirection.BACKWARD_AND_FORWARD);
        LocalDateTime departureDate = LocalDateTime.parse("2010-05-20T12:00:00");

        //WHEN
        sut.findRoutes(null, null, departureDate, offset);

        //THEN
        verify(routeSearchService).findRoutes(null, null, departureDate.minusDays(3));
        verify(routeSearchService).findRoutes(null, null, departureDate.minusDays(2));
        verify(routeSearchService).findRoutes(null, null, departureDate.minusDays(1));
        verify(routeSearchService).findRoutes(null, null, departureDate);
        verify(routeSearchService).findRoutes(null, null, departureDate.plusDays(3));
        verify(routeSearchService).findRoutes(null, null, departureDate.plusDays(2));
        verify(routeSearchService).findRoutes(null, null, departureDate.plusDays(1));
    }
}