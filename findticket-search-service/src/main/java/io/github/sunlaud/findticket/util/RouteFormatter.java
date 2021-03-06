package io.github.sunlaud.findticket.util;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RouteFormatter {
    private static final DateTimeFormatter HUMAN_READABLE_DATE = DateTimeFormat.forPattern("dd MMM HH:mm");

    public static final Function<TransportRoute, String> SINGLE_LINE = new Function<TransportRoute, String>() {
        @Override
        public String apply(TransportRoute route) {
            String freeSeats = Joiner.on(", ").withKeyValueSeparator(": ").join(route.getFreeSeatsCountByType());
            StringBuilder sb = new StringBuilder();
            sb.append(route.getId() + ": ");
            sb.append(StringUtils.rightPad(freeSeats, 16) + " (");
            sb.append(HUMAN_READABLE_DATE.print(route.getDepartureDate()) + " -> ");
            sb.append(HUMAN_READABLE_DATE.print(route.getArrivalDate()) + ", ");
            Period travelTime = route.getTravelTime();
            sb.append(String.format("%sh%sm, ", travelTime.getHours(), travelTime.minusHours(travelTime.getHours()).getMinutes()));
            sb.append(route.getName() + ")");

            return sb.toString();
        }
    };

    public static final Function<TransportRoute, String> MULTI_LINE = new Function<TransportRoute, String>() {
        @Override
        public String apply(TransportRoute route) {
            String placesPrefix = "\n - ";
            Period travelTime = route.getTravelTime();
            String header = String.format("%s (%s), %s:%s, %s -> %s [%s]" + placesPrefix,
                    route.getId(),
                    route.getName(),
                    travelTime.getHours(),
                    travelTime.minusHours(travelTime.getHours()).getMinutes(),
                    HUMAN_READABLE_DATE.print(route.getDepartureDate()),
                    HUMAN_READABLE_DATE.print(route.getArrivalDate()),
                    route.getDetailsUrl()
            );
            String freeSeats = Joiner.on(placesPrefix).withKeyValueSeparator(": ").join(route.getFreeSeatsCountByType());
            return header + freeSeats;
        }
    };

    public static String stringifyRoutes(Iterable<TransportRoute> routes, Function<TransportRoute, String> routeStringifier) {
        StringBuilder resultSb = new StringBuilder();
        for (TransportRoute route : routes) {
            resultSb.append("\n------------------------------------\n");
            resultSb.append(routeStringifier.apply(route));
        }
        return resultSb.toString();
    }



}