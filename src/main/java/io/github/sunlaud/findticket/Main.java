package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.model.FreeSeatsSummary;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.ApacheHttpClientTicketSearchService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Main {
    private    TicketSearchService ticketSearchService = new ApacheHttpClientTicketSearchService();


    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        //System.out.println(findStationsMatchingPrefix("Київ"));

        FindTrainRequest findTrainRequest = FindTrainRequest.builder()
                .departureDate("24.06.2016")
                .departureTime("22:00")
                .stationIdFrom(2210700)
                .stationIdTill(2200001)
                .build();

        searchTrainsViaApacheHttpClientImpl(findTrainRequest, "079П");
        searchTrainsViaApacheHttpClientImpl(findTrainRequest
                .withSwappedStations()
                .departureDate("02.07.2016")
                .build(), "080К");
    }

    private void searchTrainsViaApacheHttpClientImpl(FindTrainRequest findTrainRequest, String trainNumber) throws IOException {
        System.out.println("\n==========================================");
        List<Train> trains = ticketSearchService.findTrains(findTrainRequest).getTrains();
        System.out.println("Found trains:");
        trains.stream().forEach(System.out::println);
        List<FreeSeatsSummary> freeSeats = trains.stream()
                .filter(train -> trainNumber == null || train.getNumber().equals(trainNumber))
                .map(Train::getFreeSeats)
                .findFirst().get();
        System.out.println("Free seats: ");
        freeSeats.stream().forEach(System.out::println);
        System.out.println("==========================================\n");
    }
}
