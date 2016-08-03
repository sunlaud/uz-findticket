package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.apache.ApacheHttpClientTicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.feign.FeignTicketSearchServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private TicketSearchService apacheTicketSearchService = new ApacheHttpClientTicketSearchService();
    private TicketSearchService feignTicketSearchService = FeignTicketSearchServiceBuilder.getTicketSearchService();
    private final DateTimeFormatter HUMAN_READABLE = DateTimeFormatter.ofPattern("EE d MMM HH:mm");


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

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        TicketSearchService ticketSearchService = feignTicketSearchService;
//        System.out.println(ticketSearchService.findStations("Paris"));
//        System.exit(0);


        FindTrainRequest findTrainRequest = FindTrainRequest.builder()
                .departureDate(LocalDate.of(2016, 8, 10))
                .departureTime(LocalTime.of(20, 0))
                .stationIdFrom(2210700)
                .stationIdTill(2208001)
                .build();

        Predicate<LocalDate> suitableDate = new DayOfWeekFilter(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
        Predicate<Train> suitableTrain = train ->
                !train.getNumber().startsWith("42") && train.getTravelTime().toHours() < 12;
        searchTrains(ticketSearchService, findTrainRequest, 30, suitableTrain, suitableDate);
    }

    private void searchTrains(TicketSearchService service, FindTrainRequest findTrainRequest, int daysToCheckCount, Predicate<Train> suitableTrain, Predicate<LocalDate> suitableDate) throws IOException {
        LocalDate date = findTrainRequest.getDepartureDate();
        for (int i = 0; i < daysToCheckCount; i++, date = date.plusDays(1)) {
            if (!suitableDate.test(date)) {
                continue;
            }
            FindTrainRequest request = findTrainRequest.withDate(date).build();
            try {
                List<Train> trains = service.findTrains(request).getValue();
                trains.stream()
                        .filter(suitableTrain)
                        .forEach(this::printTrainInfo);
            } catch (Exception ex) {
                log.error("Error searching trains:", ex);
                System.out.println("Error searching trains for "
                        + request.getDepartureDate().atTime(request.getDepartureTime()).format(HUMAN_READABLE)
                        + " - " + ex.getMessage());
            }
        }
    }

    private void printTrainInfo(Train train) {
        String freeSeats = train.getFreeSeats().stream()
                .map(seats -> seats.getLetter() + "=" + seats.getPlaces())
                .collect(Collectors.joining(", "));
        StringBuilder sb = new StringBuilder();
        sb.append(train.getNumber()+ ": ");
        sb.append(StringUtils.rightPad(freeSeats, 16) + "(");
        sb.append(train.getFrom().getDate().format(HUMAN_READABLE)+ " -> ");
        sb.append(train.getTill().getDate().format(HUMAN_READABLE) + ", ");
        sb.append(train.getTravelTime().getSeconds() / 3600.0 + " hours, ");
        sb.append(train.getFrom().getStation() + " -> " + train.getTill().getStation() + ")");

        System.out.println(sb.toString());
    }
}
