package io.github.sunlaud.findticket.advancedplanner.filter;

import com.google.common.base.Preconditions;
import io.github.sunlaud.findticket.advancedplanner.AvailableTransportForRoute;
import io.github.sunlaud.findticket.model.TransportRoute;

import java.util.List;

public class RouteSeatsCountChecker {

    public boolean hasFreeSeats(AvailableTransportForRoute route, int requiredSeats) {
        Preconditions.checkArgument(!route.getParts().isEmpty(), "No parts in route");
        for (List<TransportRoute> transportRoutes : route.getParts()) {
            if (!haveFreeSeats(transportRoutes, requiredSeats)) {
                return false;
            }
        }
        return true;
    }

    public boolean haveFreeSeats(List<TransportRoute> transportRoutes, int requiredSeats) {
        Preconditions.checkArgument(!transportRoutes.isEmpty(), "transportRoutes should not be empty");
        for (TransportRoute transportRoute : transportRoutes) {
            if (hasFreeSeats(transportRoute, requiredSeats)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasFreeSeats(TransportRoute transportRoute, int requiredSeats) {
        for (Integer availableFreeSeats : transportRoute.getFreeSeatsCountByType().values()) {
            if (availableFreeSeats >= requiredSeats) {
                return true;
            }
        }
        return false;
    }

}
