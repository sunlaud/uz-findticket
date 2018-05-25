package io.github.sunlaud.findticket.advancedplanner.filter;

import io.github.sunlaud.findticket.advancedplanner.AvailableTransportForRoute;

public interface RouteFilter {
    AvailableTransportForRoute filter(AvailableTransportForRoute route);
}
