package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.client.dto.CoachDto;
import io.github.sunlaud.findticket.client.dto.TrainDto;
import io.github.sunlaud.findticket.client.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.request.GetCoachesRequest;
import io.github.sunlaud.findticket.client.response.GetCoachesResponse;
import io.github.sunlaud.findticket.client.service.TicketSearchService;
import io.github.sunlaud.findticket.client.service.impl.apache.ApacheHttpClientTicketSearchService;
import io.github.sunlaud.findticket.client.service.impl.feign.FeignTicketSearchServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
                .departureDate(LocalDate.now().plusDays(10))
                .departureTime(LocalTime.of(0, 0))
                .stationIdFrom(2210700)
                .stationIdTill(2208001)
                .build();


        GetCoachesRequest getCoachesRequest = GetCoachesRequest.builder()
                .departureDate(LocalDate.of(2016, 9, 2).atTime(0, 0))
                .stationIdFrom(2210700)
                .stationIdTill(2208001)
                .build();

        findTrainRequest = findTrainRequest.toBuilder().build();


        Predicate<LocalDate> suitableDate = new DayOfWeekFilter(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
        Predicate<TrainDto> suitableTrain = train ->
                !train.getNumber().startsWith("42") && train.getTravelTime().toHours() < 15;
        searchTrains(ticketSearchService, findTrainRequest, 10, suitableTrain, suitableDate);
    }

    private void searchTrains(TicketSearchService service, FindTrainRequest findTrainRequest, int daysToCheckCount, Predicate<TrainDto> suitableTrain, Predicate<LocalDate> suitableDate) throws IOException {
        LocalDate date = findTrainRequest.getDepartureDate();
        for (int i = 0; i < daysToCheckCount; i++, date = date.plusDays(1)) {
            if (!suitableDate.test(date)) {
                continue;
            }
            FindTrainRequest request = findTrainRequest.toBuilder().departureDate(date).build();
            try {
                List<TrainDto> trains = service.findTrains(request).getValue();
                trains.stream()
                        .filter(suitableTrain)
                        .forEach(train -> {
//                            Map<String, List<CoachDto>> coaches = train.getFreeSeats().stream()
//                                    .map(FreeSeatsDto::getLetter)
//                                    .map(coachType -> getCoaches(service, train, coachType))
//                                    .flatMap(c -> c.getCoaches().stream())
//                                    .collect(Collectors.groupingBy(CoachDto::getType));
                            printTrainInfo(train, null);
                        });
            } catch (Exception ex) {
                log.error("Error searching trains:", ex);
                System.out.println("Error searching trains for "
                        + request.getDepartureDate().atTime(request.getDepartureTime()).format(HUMAN_READABLE)
                        + " - " + ex.getMessage());
            }
        }
    }

    private GetCoachesResponse getCoaches(TicketSearchService service, TrainDto train, String coachType) {
        GetCoachesRequest getCoachesRequest = GetCoachesRequest.builder()
                .stationIdFrom(train.getFrom().getStationId())
                .stationIdTill(train.getTill().getStationId())
                .trainNumber(train.getNumber())
                .departureDateAsEpochSecond(train.getFrom().getDateAsEpochSecond())
                .coachType(coachType)
                .build();
       return service.getCoaches(getCoachesRequest);
    }

    private void printTrainInfo(TrainDto train, Map<String, List<CoachDto>> coaches) {
        String freeSeats = train.getFreeSeats().stream()
                .map(seats -> seats.getLetter() + "=" + seats.getPlaces())
                .collect(Collectors.joining(", "));
        StringBuilder sb = new StringBuilder();
        sb.append(train.getNumber()+ ": ");
        sb.append(StringUtils.rightPad(freeSeats, 16) + "(");
        sb.append(train.getFrom().getDate().format(HUMAN_READABLE)+ " -> ");
        sb.append(train.getTill().getDate().format(HUMAN_READABLE) + ", ");
        sb.append(String.format("%sh%sm, ", train.getTravelTime().toHours(), train.getTravelTime().minusHours(train.getTravelTime().toHours()).toMinutes()));
        sb.append(train.getFrom().getStation() + " -> " + train.getTill().getStation() + ")");

        System.out.println(sb.toString());
    }
}
