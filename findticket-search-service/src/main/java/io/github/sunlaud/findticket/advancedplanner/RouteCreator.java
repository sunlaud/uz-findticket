package io.github.sunlaud.findticket.advancedplanner;

import io.github.sunlaud.findticket.model.Station;

import java.util.ArrayList;
import java.util.List;

public class RouteCreator {
    private final String[] allExistingStationNames;
    private final StationsCache stationsCache;

    public RouteCreator(String[] allExistingStationNames, StationsCache stationsCache) {
        this.allExistingStationNames = allExistingStationNames;
        this.stationsCache = stationsCache;

    }

    public List<Route<Station>> findRoutes(String departureStationName, String arrivalStationName) {
        List<Route<Station>> routes = new ArrayList<>(allExistingStationNames.length);
        for (String connectionStation : allExistingStationNames) {
            if (!connectionStation.equals(departureStationName) && !connectionStation.equals(arrivalStationName)) {
                routes.add(new Route<>(
                        stationsCache.getStationByExactName(departureStationName),
                        stationsCache.getStationByExactName(connectionStation),
                        stationsCache.getStationByExactName(arrivalStationName)));
            }
        }
        return routes;
    }
}
