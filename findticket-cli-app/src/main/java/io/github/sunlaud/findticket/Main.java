package io.github.sunlaud.findticket;


import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.api.SeatsSummary;
import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.TransportRoute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.List;

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
        try {
            Station from = fromAll.get(0);
            Station till = tillAll.get(0);
            Iterable<TransportRoute> routes = routeSearchService.findRoutes(from, till, departure);
            System.out.println(String.format("Trains travelling %s -> %s:", from.getName(), till.getName()));
            for (TransportRoute route : routes) {
                printTrainInfo(route);
            }
        } catch (Exception ex) {
            log.error("Error searching trains:", ex);
            System.err.println("Error searching trains for "
                    + HUMAN_READABLE.print(departure) + " - " + ex.getMessage());
        }
    }


    private void printTrainInfo(TransportRoute route) {
        String freeSeats = Joiner.on(", ").join(Iterables.transform(route.getFreeSeats(), new Function<SeatsSummary, String>() {
            @Override
            public String apply(SeatsSummary seats) {
                return String.format("%s (%s) = %s", seats.getName(), seats.getId(), seats.getPlaces());
            }
        }));
        StringBuilder sb = new StringBuilder();
        sb.append(route.getId()+ ": ");
        sb.append(StringUtils.rightPad(freeSeats, 16) + "(");
        sb.append(HUMAN_READABLE.print(route.getDepartureDate()) + " -> ");
        sb.append(HUMAN_READABLE.print(route.getArrivalDate()) + ", ");
        Period travelTime = route.getTravelTime().toPeriod();
        sb.append(String.format("%sh%sm, ", travelTime.getHours(), travelTime.minusHours(travelTime.getHours()).getMinutes()));
        sb.append(route.getName() + ")");
        System.out.println(sb.toString());

        //System.out.println(routeSearchService.getCoaches(train));
    }
}
