package io.github.sunlaud.findticket.advancedplanner.filter;

import io.github.sunlaud.findticket.advancedplanner.SingleConnectionTransportRoute;
import lombok.Data;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
public class ConnectionTimeTransportRouteFilter {
    private final Duration minConnectionTime = Duration.standardMinutes(5);
    private final Duration maxConnectionTime = Duration.standardHours(7);

    public List<SingleConnectionTransportRoute> excludeTooShortAndTooLong(Collection<SingleConnectionTransportRoute> source) {
        List<SingleConnectionTransportRoute> result = new ArrayList<>(source.size());
        for (SingleConnectionTransportRoute route : source) {
            Duration waitTime = route.getConnectionWaitTime();
            if (!waitTime.isLongerThan(maxConnectionTime) && !waitTime.isShorterThan(minConnectionTime)) {
                result.add(route);
            }
        }
        return result;
    }
}
