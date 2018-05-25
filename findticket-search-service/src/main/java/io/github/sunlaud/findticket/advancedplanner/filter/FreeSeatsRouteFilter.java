package io.github.sunlaud.findticket.advancedplanner.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import io.github.sunlaud.findticket.advancedplanner.AvailableTransportForRoute;
import io.github.sunlaud.findticket.model.TransportRoute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FreeSeatsRouteFilter implements RouteFilter {
    private final int requiredFreeSeats = 2;

    @Override
    public AvailableTransportForRoute filter(AvailableTransportForRoute route) {
        return new AvailableTransportForRoute(route.getRoute(), filterParts(route.getParts()));
    }

    private List<? extends List<TransportRoute>> filterParts(List<? extends List<TransportRoute>> parts) {
        List<List<TransportRoute>> filteredParts = new ArrayList<>(parts.size());
        for (List<TransportRoute> part : parts) {
            filteredParts.add(filterPart(part));
        }
        return filteredParts;
    }

    private List<TransportRoute> filterPart(List<TransportRoute> part) {
        Collection<TransportRoute> filtered = Collections2.filter(part, new Predicate<TransportRoute>() {
            @Override
            public boolean apply(TransportRoute input) {
                return RouteSeatsCountChecker.hasFreeSeats(input, requiredFreeSeats);
            }
        });
        return new ArrayList<>(filtered);
    }
}
