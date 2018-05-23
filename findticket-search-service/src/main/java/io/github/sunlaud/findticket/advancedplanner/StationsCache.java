package io.github.sunlaud.findticket.advancedplanner;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.sunlaud.findticket.RouteSearchService;
import io.github.sunlaud.findticket.model.Station;

import java.util.List;

public class StationsCache {
    private final RouteSearchService routeSearchService;
    private final LoadingCache<String, Station> stationsCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new CacheLoader<String, Station>() {
                public Station load(String key) {
                    return loadStationByExactName(key);
                }
            });

    public StationsCache(RouteSearchService routeSearchService) {
        this.routeSearchService = routeSearchService;
    }

    private Station loadStationByExactName(String stationName) {
        List<Station> stations = routeSearchService.findStations(stationName);
        for (Station station : stations) {
            if (station.getName().equals(stationName)) {
                return station;
            }
        }
        throw new RuntimeException("Station '" + stationName + "' not found");
    }


    public Station getStationByExactName(String stationName) {
        return stationsCache.getUnchecked(stationName);
    }
}
