package io.github.sunlaud.findticket.advancedplanner;

import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import io.github.sunlaud.findticket.util.RouteFormatter;
import lombok.Data;

import java.util.List;

@Data
public class ComplexRoute {
    private final Route<Station> route;
    private final List<? extends List<TransportRoute>> parts;

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
