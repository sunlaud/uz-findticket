package io.github.sunlaud.findticket.advancedplanner;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Route<T> implements Comparable<Route<T>> {
    private final T departureStation;
    private final T connectionStation;
    private final T arrivalStation;
    private final long straightTotalDistanceKm;

    @Override
    public int compareTo(Route<T> other) {
        return Long.compare(this.straightTotalDistanceKm, other.straightTotalDistanceKm);
    }

    @Override
    public String toString() {
        return "(" + departureStation + " -> " + connectionStation +  " -> " + arrivalStation + ")" ;
    }
}
