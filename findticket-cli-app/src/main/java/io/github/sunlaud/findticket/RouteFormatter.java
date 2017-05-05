package io.github.sunlaud.findticket;

import com.google.common.base.Joiner;
import io.github.sunlaud.findticket.model.TransportRoute;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RouteFormatter {
    private static final DateTimeFormatter HUMAN_READABLE = DateTimeFormat.forPattern("EE d MMM HH:mm");

    public String stringifySearhcResult(Iterable<TransportRoute> routes) {
        StringBuilder resultSb = new StringBuilder();

//
//        resultSb.append(String.format("\n===== Filtered %s =====\n", LocalDateTime.now()));
//        if (Iterables.isEmpty(routesFiltered)) {
//            resultSb.append("no routes match criteria " + criteria + "\n");
//        }
//        for (TransportRoute route : routesFiltered) {
//            resultSb.append(stringifyTrainInfo(route));
//        }
//

//        resultSb.append(String.format("\n==== All routes %s -> %s: ====", from.getName(), till.getName()));
        for (TransportRoute route : routes) {
            resultSb.append("\n------------------------------------\n");
            resultSb.append(stringifyTrainInfo(route));
        }
        return resultSb.toString();
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