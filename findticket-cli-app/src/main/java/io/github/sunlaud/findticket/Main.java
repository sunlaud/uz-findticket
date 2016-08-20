package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.Train;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private final DateTimeFormatter HUMAN_READABLE = DateTimeFormatter.ofPattern("EE d MMM HH:mm");
    private final TrainSearchServiceUz trainSearchService = new TrainSearchServiceUz();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() {
            List<Station> from = trainSearchService.findStations("Дніпропетровськ-Голов");
            System.out.println(from);
            List<Station> till = trainSearchService.findStations("Одеса");
            System.out.println(till);
            LocalDateTime departure = LocalDate.now().plusDays(10).atStartOfDay();
        try {
            List<Train> trains = trainSearchService.findTrains(from.get(0), till.get(0), departure);
            trains.forEach(this::printTrainInfo);
        } catch (Exception ex) {
            log.error("Error searching trains:", ex);
            System.out.println("Error searching trains for "
                    + departure.format(HUMAN_READABLE)
                    + " - " + ex.getMessage());
        }
    }


    private void printTrainInfo(Train train) {
        String freeSeats = train.getFreeSeats().stream()
                .map(seats -> seats.getLetter() + "=" + seats.getPlaces())
                .collect(Collectors.joining(", "));
        StringBuilder sb = new StringBuilder();
        sb.append(train.getNumber()+ ": ");
        sb.append(StringUtils.rightPad(freeSeats, 16) + "(");
        sb.append(train.getDepartureDate().format(HUMAN_READABLE) + " -> ");
        sb.append(train.getArrivalDate().format(HUMAN_READABLE) + ", ");
        sb.append(String.format("%sh%sm, ", train.getTravelTime().toHours(), train.getTravelTime().minusHours(train.getTravelTime().toHours()).toMinutes()));
        sb.append(train.getStationFrom().getTitle() + " -> " + train.getStationTill().getTitle() + ")");

        System.out.println(sb.toString());
    }

    private static class DayOfWeekFilter implements Predicate<LocalDate> {
        private final List<DayOfWeek> daysOfWeek;

        private DayOfWeekFilter(DayOfWeek... daysOfWeek) {
            this.daysOfWeek = Arrays.asList(daysOfWeek);
        }

        @Override
        public boolean test(LocalDate date) {
            return daysOfWeek.stream().anyMatch(dayOfWeek -> dayOfWeek == date.getDayOfWeek());
        }
    }

}
