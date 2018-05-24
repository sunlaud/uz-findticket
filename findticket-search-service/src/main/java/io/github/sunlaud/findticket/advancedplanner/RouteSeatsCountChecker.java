package io.github.sunlaud.findticket.advancedplanner;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import io.github.sunlaud.findticket.RouteSearchService;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;

public class RouteSeatsCountChecker {
    private final RouteSearchService routeSearchService;

    public RouteSeatsCountChecker(RouteSearchService routeSearchService) {
        this.routeSearchService = routeSearchService;
    }

    public boolean hasFreeSeats(ComplexRoute route) {
        for (List<TransportRoute> transportRoutes : route.getParts()) {
            if (transportRoutes.stream().allMatch(noSeatsInside())) {
                return false;
            }
        }
        return true;
    }

    private Predicate<? super TransportRoute> noSeatsInside() {
        return transportRoute -> allEmpty(transportRoute.getFreeSeatsCountByType());
    }

    private boolean allEmpty(Map<SeatType, Integer> freeSeatsCountByType) {
        return freeSeatsCountByType.values().stream().noneMatch(integer -> integer > 0);
    }

}
