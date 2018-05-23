package io.github.sunlaud.findticket;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.client.uz.UzTrainRouteSearchService;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.or;
import static io.github.sunlaud.findticket.filtering.Filters.hasMoreThan;
import static io.github.sunlaud.findticket.util.RouteFormatter.MULTI_LINE;
import static io.github.sunlaud.findticket.util.RouteFormatter.stringifyRoutes;

@Slf4j
public class Main {
    private final RouteSearchService routeSearchService = new UzTrainRouteSearchService();
    private final TripPlanner tripPlanner = new TripPlanner(routeSearchService);

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() {

        String[] stations = {
                "Бердичів",
                "Бердянськ",
                "Вінниця",
                "Горлівка",
                "Дніпродзержинськ",
                "Дніпро",
                "Донецьк",
                "Житомир",
                "Запоріжжя",
                "Запоріжжя",
                "Івано-Франківськ",
                "Київ",
                "Київ",
                "Кіровоград",
                "Кривий Ріг",
                "Луганськ",
                "Луцьк",
                "Львів",
                "Микитівка",
                "Миколаїв",
                "Одеса",
                "Полтава",
                "Полтава",
                "Рівне",
                "Суми",
                "Тернопіль",
                "Ужгород",
                "Харків",
                "Херсон",
                "Хмельницький",
                "Черкаси",
                "Чернігів",
                "Чернівці"
        };


        Set<String> uniqueStations = new HashSet<>(Arrays.asList(stations));
        for (int i = 0; i < uniqueStations.size(); i++) {
            List<Station> found = routeSearchService.findStations(stations[i]);
            for (Station station : found) {
                System.out.println("\"" + station.getName() + "\",");
            }
        }

        if (true) return;


        List<Station> fromAll = routeSearchService.findStations("Дніпро-Головний");
            System.out.println(fromAll);
            List<Station> tillAll = routeSearchService.findStations("Львів");
            System.out.println(tillAll);
            LocalDateTime departure = LocalDate.now().plusDays(10).toDateTimeAtStartOfDay().toLocalDateTime();
//            departure = LocalDateTime.parse("2017-10-22T00:00");
        try {
            Station from = fromAll.get(0);
            Station till = tillAll.get(0);
            Iterable<TransportRoute> routes = tripPlanner.findRoutes(from, till, departure, new DaysOffset(3, OffsetDirection.BACKWARD));
            LocalDateTime arrivalDateTime = LocalDateTime.parse("2017-05-19T08:30");
            Predicate<TransportRoute> criteria = and(
                    //arrivingBefore(arrivalDateTime),
                    or(
                        hasMoreThan(35).freeSeatsWithTypeId(new SeatType("П")),
                        hasMoreThan(17).freeSeatsWithTypeId(new SeatType("К"))
                    )
            );
            criteria = Predicates.alwaysTrue();
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
            ex.printStackTrace(System.err);
        }
    }
}
