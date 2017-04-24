package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.Train;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private final DateTimeFormatter HUMAN_READABLE = DateTimeFormatter.ofPattern("EE d MMM HH:mm");
    private final TrainSearchService trainSearchService = new TrainSearchServiceUz();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() {
            List<Station> fromAll = trainSearchService.findStations("Запоріжжя 1");
            System.out.println(fromAll);
            List<Station> tillAll = trainSearchService.findStations("Хмельницький");
            System.out.println(tillAll);
            LocalDateTime departure = LocalDate.now().plusDays(10).atStartOfDay();
            departure = LocalDate.of(2017, 5, 18).atStartOfDay();
//            departure = LocalDate.of(2017, 5, 21).atStartOfDay();
        try {
            Station from = fromAll.get(0);
            Station till = tillAll.get(0);
            List<Train> trains = trainSearchService.findTrains(from, till, departure);
            System.out.println(String.format("Trains travelling %s -> %s:", from.getName(), till.getName()));
            trains.forEach(this::printTrainInfo);
        } catch (Exception ex) {
            log.error("Error searching trains:", ex);
            System.err.println("Error searching trains for "
                    + departure.format(HUMAN_READABLE) + " - " + ex.getMessage());
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
        sb.append(train.getRouteStartStationName() + " - " + train.getRouteEndStationName() + ")");
        System.out.println(sb.toString());

        //System.out.println(trainSearchService.getCoaches(train));
    }
}
