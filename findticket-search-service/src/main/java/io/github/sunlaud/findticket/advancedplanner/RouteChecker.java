package io.github.sunlaud.findticket.advancedplanner;

import io.github.sunlaud.findticket.RouteSearchService;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RouteChecker {
    private final RouteSearchService routeSearchService;

    public RouteChecker(RouteSearchService routeSearchService) {
        this.routeSearchService = routeSearchService;
    }

    public ComplexRoute check(Route<Station> route) {
        Collection<TransportRoute> routes1 = routeSearchService.findRoutes(
                route.getDepartureStation(), route.getConnectionStation(), LocalDateTime.parse("2018-06-02T5:00"));
        if (routes1.isEmpty()) {
            return null;
        }
        Collection<TransportRoute> routes2 = routeSearchService.findRoutes(
                route.getConnectionStation(), route.getArrivalStation(), LocalDateTime.parse("2018-06-02T5:00"));
        Collection<TransportRoute> routes2Tomorrow = routeSearchService.findRoutes(
                route.getConnectionStation(), route.getArrivalStation(), LocalDateTime.parse("2018-06-03T5:00"));
        routes2.addAll(routes2Tomorrow);
        if (routes2.isEmpty()) {
            return null;
        }
        return new ComplexRoute(route, Arrays.asList(new ArrayList<TransportRoute>(routes1), new ArrayList<TransportRoute>(routes2)));
    }

}
