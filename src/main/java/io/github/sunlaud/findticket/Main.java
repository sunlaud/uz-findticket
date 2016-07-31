package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.apache.ApacheHttpClientTicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.feign.FeignTicketSearchServiceBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private TicketSearchService apacheTicketSearchService = new ApacheHttpClientTicketSearchService();
    private TicketSearchService feignTicketSearchService = FeignTicketSearchServiceBuilder.getTicketSearchService();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        TicketSearchService ticketSearchService = feignTicketSearchService;
//        System.out.println(ticketSearchService.findStations("Львів"));
//        System.out.println(ticketSearchService.findStations("Запоріжжя"));
//        System.exit(0);


        FindTrainRequest findTrainRequest = FindTrainRequest.builder()
                .departureDate(LocalDate.of(2016, 8, 29))
//                .departureDate("26.08.2016")
                .departureTime("00:00")
                .stationIdFrom(2218000)
                .stationIdTill(2210800)
                .build();

        searchTrains(ticketSearchService, findTrainRequest, 1, "070");
    }

    private void searchTrains(TicketSearchService service, FindTrainRequest findTrainRequest, int daysToCheck, String trainCode) throws IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM HH:mm");
        LocalDate date = findTrainRequest.getDepartureDate();
        for (int i = 0; i < daysToCheck; i++, date = date.plusDays(1)) {
            FindTrainRequest request = findTrainRequest.withDate(date).build();
            try {
                List<Train> trains = service.findTrains(request).getTrains();
                String nowFormatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
                System.out.println("=======" + nowFormatted + "====="  + date + ", found trains: "
                        + trains.stream().map(train ->
                        train.getNumber()
                                + " (" + train.getFreeSeats().stream()
                                .map(seats -> seats.getLetter() + ": " + seats.getPlaces())
                                .collect(Collectors.joining(", "))
                                + ")").collect(Collectors.joining(", ")) + "====================");
                trains.stream()
                        .filter(train -> trainCode == null || train.getNumber().startsWith(trainCode))
                        .forEach(train -> {
                            System.out.println(train.getNumber()
                                            + ": " + train.getFrom().getStation()
                                            + " -> " + train.getTill().getStation()
                                            + ", " + train.getFrom().getDate().format(dateFormatter)
                                            + " -> " + train.getTill().getDate().format(dateFormatter)
                            );
                            train.getFreeSeats().forEach(seats -> {
                                System.out.print(seats.getLetter() + ": " + seats.getPlaces() + "\t");
                            });
                            System.out.println();
                        });
            } catch (Exception ex) {
                log.error("Error searching trains, maybe there is no trains", ex);
                System.out.println("Error searching trains: " + ex.getMessage());
            }
        }
    }

}
