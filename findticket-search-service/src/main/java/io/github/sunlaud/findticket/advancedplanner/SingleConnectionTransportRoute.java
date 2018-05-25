package io.github.sunlaud.findticket.advancedplanner;

import com.google.common.base.Preconditions;
import io.github.sunlaud.findticket.model.TransportRoute;
import io.github.sunlaud.findticket.util.RouteFormatter;
import lombok.Data;
import org.joda.time.Duration;


@Data
public class SingleConnectionTransportRoute {
    private final TransportRoute part1;
    private final TransportRoute part2;
    private final Duration connectionWaitTime;
    private final Duration totalTravelTime;

    public SingleConnectionTransportRoute(TransportRoute part1, TransportRoute part2) {
        Preconditions.checkArgument(part1.getTo().equals(part2.getFrom()), "part1 'to' should be equal to part2 'from'");
        this.part1 = part1;
        this.part2 = part2;
        connectionWaitTime = new Duration(part1.getArrivalDate().toDateTime().toInstant(), part2.getDepartureDate().toDateTime().toInstant());
        totalTravelTime = connectionWaitTime.plus(part1.getTravelTime()).plus(part2.getTravelTime());
    }

    @Override
    public String toString() {
        return "=================== "
                + part1.getFrom().getName() + " -> " + part1.getTo().getName() + " -> " + part2.getTo().getName() + " =====================\n"
                + "   " + RouteFormatter.SINGLE_LINE.apply(part1) + "\n"
                + "   " + RouteFormatter.SINGLE_LINE.apply(part2) + "\n"
                + "   " + "connection wait: " + RouteFormatter.formatDuration(connectionWaitTime) + ", total travel time: " + RouteFormatter.formatDuration(totalTravelTime);
    }
}
