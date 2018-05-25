package io.github.sunlaud.findticket.advancedplanner;

import com.google.common.base.Preconditions;
import io.github.sunlaud.findticket.model.TransportRoute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransportRoutesComposer {

    public List<SingleConnectionTransportRoute> compose(Collection<AvailableTransportForRoute> source) {
        List<SingleConnectionTransportRoute> result = new ArrayList<>();
        for (AvailableTransportForRoute availableTransportForRoute : source) {
            result.addAll(compose(availableTransportForRoute));
        }
        return result;
    }

    public Collection<SingleConnectionTransportRoute> compose(AvailableTransportForRoute availableTransportForRoute) {
        Preconditions.checkArgument(availableTransportForRoute.getParts().size() == 2,
                "support routes with exactly 2 parts, but got with %s", availableTransportForRoute.getParts().size());
        List<TransportRoute> part1 = availableTransportForRoute.getParts().get(0);
        List<TransportRoute> part2 = availableTransportForRoute.getParts().get(1);
        List<SingleConnectionTransportRoute> result = new ArrayList<>(part1.size() * part2.size());
        for (TransportRoute first : part1) {
            for (TransportRoute second : part2) {
                result.add(new SingleConnectionTransportRoute(first, second));
            }
        }
        return result;
    }
}
