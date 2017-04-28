package io.github.sunlaud.findticket;


import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.client.uz.UzTrainRouteSearchService;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Predicates.and;
import static io.github.sunlaud.findticket.filtering.Filters.arrivingBefore;
import static io.github.sunlaud.findticket.filtering.Filters.hasMoreThan;

@Slf4j
public class Main {
    private final DateTimeFormatter HUMAN_READABLE = DateTimeFormat.forPattern("EE d MMM HH:mm");
    private final RouteSearchService routeSearchService = new UzTrainRouteSearchService();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() {
            List<Station> fromAll = routeSearchService.findStations("Запоріжжя 1");
            System.out.println(fromAll);
            List<Station> tillAll = routeSearchService.findStations("Хмельницький");
            System.out.println(tillAll);
            LocalDateTime departure = LocalDate.now().plusDays(10).toDateTimeAtStartOfDay().toLocalDateTime();
            departure = LocalDate.parse("2017-05-18").toDateTimeAtStartOfDay().toLocalDateTime();
//            departure = LocalDate.of(2017, 5, 21).atStartOfDay();
        
        StringBuilder resultSb = new StringBuilder();
        try {
            Station from = fromAll.get(0);
            Station till = tillAll.get(0);
            Iterable<TransportRoute> routes = routeSearchService.findRoutes(from, till, departure);
            LocalDateTime arrivalDateTime = LocalDateTime.parse("2017-05-19T08:30");
            Predicate<TransportRoute> criteria = and(
                    arrivingBefore(arrivalDateTime),
                    hasMoreThan(35).freeSeatsWithTypeId(new SeatType("П")),
                    hasMoreThan(17).freeSeatsWithTypeId(new SeatType("К")));
            Iterable<TransportRoute> routesFiltered = Iterables.filter(routes, criteria);

            resultSb.append(String.format("\n===== Filtered %s =====\n", LocalDateTime.now()));
            if (Iterables.isEmpty(routesFiltered)) {
                resultSb.append("no routes match criteria " + criteria + "\n");
            }
            for (TransportRoute route : routesFiltered) {
                resultSb.append(stringifyTrainInfo(route));
            }
            resultSb.append(String.format("\n==== All routes %s -> %s: ====", from.getName(), till.getName()));
            for (TransportRoute route : routes) {
                resultSb.append("\n------------------------------------\n");
                resultSb.append(stringifyTrainInfo(route));
            }
            System.out.println(resultSb.toString());
        } catch (Exception ex) {
            log.error("Error searching trains:", ex);
            System.err.println("Error searching trains for "
                    + HUMAN_READABLE.print(departure) + " - " + ex.getMessage());
        }
    }


    private String stringifyTrainInfo(TransportRoute route) {
        String freeSeats = Joiner.on(", ").withKeyValueSeparator(": ").join(route.getFreeSeatsCountByType());
        StringBuilder sb = new StringBuilder();
        sb.append(route.getId()+ ": ");
        sb.append(StringUtils.rightPad(freeSeats, 16) + "(");
        sb.append(HUMAN_READABLE.print(route.getDepartureDate()) + " -> ");
        sb.append(HUMAN_READABLE.print(route.getArrivalDate()) + ", ");
        Period travelTime = route.getTravelTime().toPeriod();
        sb.append(String.format("%sh%sm, ", travelTime.getHours(), travelTime.minusHours(travelTime.getHours()).getMinutes()));
        sb.append(route.getName() + ")");

        return sb.toString();
        //System.out.println(routeSearchService.getCoaches(train));
    }
}
