package io.github.sunlaud.findticket.advancedplanner;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

import io.github.sunlaud.findticket.RouteSearchService;
import io.github.sunlaud.findticket.filtering.Filters;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AvailableTransportFinder {
    private final RouteSearchService routeSearchService;

    public AvailableTransportFinder(RouteSearchService routeSearchService) {
        this.routeSearchService = routeSearchService;
    }

    public Optional<AvailableTransportForRoute> findTransport(Route<Station> route) {
        Collection<TransportRoute> routes1 = routeSearchService.findRoutes(
                route.getDepartureStation(), route.getConnectionStation(), LocalDateTime.parse("2018-06-02T16:00"));
        if (routes1.isEmpty()) {
            return Optional.absent();
        }
        Collection<TransportRoute> routes2 = routeSearchService.findRoutes(
                route.getConnectionStation(), route.getArrivalStation(), LocalDateTime.parse("2018-06-02T16:00"));
        Collection<TransportRoute> routes2Tomorrow = routeSearchService.findRoutes(
                route.getConnectionStation(), route.getArrivalStation(), LocalDateTime.parse("2018-06-03T00:00"));
        routes2Tomorrow = Collections2.filter(routes2Tomorrow, Filters.arrivingBefore(LocalDateTime.parse("2018-06-04T00:00")));
        routes2.addAll(routes2Tomorrow);
        if (routes2.isEmpty()) {
            return Optional.absent();
        }
        return Optional.of(new AvailableTransportForRoute(route, Arrays.asList(new ArrayList<>(routes1), new ArrayList<>(routes2))));
    }

}
