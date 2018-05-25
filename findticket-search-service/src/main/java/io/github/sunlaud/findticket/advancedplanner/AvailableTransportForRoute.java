package io.github.sunlaud.findticket.advancedplanner;

import com.google.common.base.Preconditions;

import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import io.github.sunlaud.findticket.util.RouteFormatter;
import lombok.Data;

import java.util.List;

@Data
public class AvailableTransportForRoute {
    private final Route<Station> route;
    private final List<? extends List<TransportRoute>> parts;

    public AvailableTransportForRoute(Route<Station> route, List<? extends List<TransportRoute>> parts) {
        for (List<TransportRoute> part : parts) {
            Preconditions.checkArgument(!part.isEmpty(), "Part can not be empty");
        }
        this.route = route;
        this.parts = parts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(route.toString());
        for (List<TransportRoute> part : parts) {
            sb.append('\n').append("part\n");
            for (TransportRoute transportRoute : part) {
                sb.append("   ").append(RouteFormatter.SINGLE_LINE.apply(transportRoute)).append('\n');
            }
        }
        return sb.toString();
    }
}
