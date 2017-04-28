package io.github.sunlaud.findticket.filtering;


import com.google.common.base.Predicate;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.Data;
import org.joda.time.LocalDateTime;

public class Filters {
    public static Predicate<TransportRoute> arrivingBefore(LocalDateTime date) {
        return new ArrivingBefore(date);
    }

    public static ComparablePredicateBuilder hasMoreThan(int amount) {
        return new ComparablePredicateBuilder(amount);
    }


    @Data
     public static class ComparablePredicateBuilder {
        private final int amount;

        public Predicate<TransportRoute> freeSeatsWithTypeId(SeatType seatType) {
            return new FreeSeatsGreaterThan(amount, seatType);
        }
    }

    @Data
    private static class ArrivingBefore implements Predicate<TransportRoute> {
        private final LocalDateTime targetArrivalDateTime;

        @Override
        public boolean apply(TransportRoute route) {
            return targetArrivalDateTime.isAfter(route.getArrivalDate());
        }

        @Override
        public String toString() {
            return "arriving before " + targetArrivalDateTime;
        }
    }

    @Data
    private static class FreeSeatsGreaterThan implements Predicate<TransportRoute> {
        private final Integer minFreeSetsCount;
        private final SeatType seatType;

        @Override
        public String toString() {
            return "more than " + minFreeSetsCount + " free seats of type '" + seatType + "'";
        }

        @Override
        public boolean apply(TransportRoute route) {
            Integer freeSeatsCount = route.getFreeSeatsCountByType().get(seatType);
            return freeSeatsCount != null && freeSeatsCount > minFreeSetsCount;
        }
    }
}
