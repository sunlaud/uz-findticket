package io.github.sunlaud.findticket;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.client.uz.UzTrainRouteSearchService;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.or;
import static io.github.sunlaud.findticket.filtering.Filters.hasMoreThan;
import static io.github.sunlaud.findticket.util.RouteFormatter.MULTI_LINE;
import static io.github.sunlaud.findticket.util.RouteFormatter.stringifyRoutes;

@Slf4j
public class Main {
    private final RouteSearchService routeSearchService = new UzTrainRouteSearchService();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() {
            List<Station> fromAll = routeSearchService.findStations("Хмельницький");
            System.out.println(fromAll);
            List<Station> tillAll = routeSearchService.findStations("Запоріжжя 1");
            System.out.println(tillAll);
            LocalDateTime departure = LocalDate.now().plusDays(10).toDateTimeAtStartOfDay().toLocalDateTime();
            departure = LocalDateTime.parse("2017-05-21T20:00");
        try {
            Station from = fromAll.get(0);
            Station till = tillAll.get(0);
            Iterable<TransportRoute> routes = routeSearchService.findRoutes(from, till, departure);
            LocalDateTime arrivalDateTime = LocalDateTime.parse("2017-05-19T08:30");
            Predicate<TransportRoute> criteria = and(
                    //arrivingBefore(arrivalDateTime),
                    or(
                        hasMoreThan(35).freeSeatsWithTypeId(new SeatType("П")),
                        hasMoreThan(17).freeSeatsWithTypeId(new SeatType("К"))
                    )
            );
            Iterable<TransportRoute> routesFiltered = Iterables.filter(routes, criteria);

            System.out.println(String.format("\n===== Filtered %s =====\n", LocalDateTime.now()));
            if (Iterables.isEmpty(routesFiltered)) {
                System.out.println("no routes match criteria " + criteria + "\n");
            }
            System.out.println(stringifyRoutes(routesFiltered, MULTI_LINE));
            System.out.println(String.format("\n==== All routes %s -> %s: ====", from.getName(), till.getName()));
            System.out.println(stringifyRoutes(routes, MULTI_LINE));
        } catch (Exception ex) {
            log.error("Error searching trains:", ex);
            System.err.println("Error searching trains - " + ex.getMessage());
        }
    }
}
