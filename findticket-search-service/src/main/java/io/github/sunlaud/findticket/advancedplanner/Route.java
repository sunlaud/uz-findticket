package io.github.sunlaud.findticket.advancedplanner;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class  Route<T> {
    private final T departureStation;
    private final T connectionStation;
    private final T arrivalStation;

    @Override
    public String toString() {
        return "(" + departureStation + " -> " + connectionStation +  " -> " + arrivalStation + ")" ;
    }
}
