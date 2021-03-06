package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;

import java.util.*;

@Slf4j
public class TripPlanner {
    public static final Comparator<TransportRoute> TRAVEL_TIME_ROUTE_COMPARATOR = new Comparator<TransportRoute>() {
        @Override
        public int compare(TransportRoute o1, TransportRoute o2) {
            int byTravelTime = o1.getTravelTime().toStandardDuration()
                    .compareTo(o2.getTravelTime().toStandardDuration());
            if (byTravelTime == 0) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
            return byTravelTime;
        }
    };
    private final RouteSearchService routeSearchService;

    public TripPlanner(RouteSearchService routeSearchService) {
        this.routeSearchService = routeSearchService;
    }

    /**
     *
     * @throws RouteSearchException if something went wrong
     */
    public List<TransportRoute> findRoutes(Station stationFrom, Station stationTo, LocalDateTime departureDate, DaysOffset offset) {
        Set<TransportRoute> routes = new HashSet<>();
        for (LocalDateTime date : offset.applyTo(departureDate)) {
            try {
                Collection<TransportRoute> found = routeSearchService.findRoutes(stationFrom, stationTo, date);
                routes.addAll(found);
            } catch (RouteSearchException e) {
                throw e;
            } catch (Exception e) {
                throw new RouteSearchException("Error searching trains", e);
            }
        }
        return new ArrayList<>(routes);
    }


}
