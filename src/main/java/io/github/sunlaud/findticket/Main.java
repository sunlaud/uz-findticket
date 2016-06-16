package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.model.FreeSeatsSummary;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.ApacheHttpClientTicketSearchService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


        List<Train> trains = ticketSearchService.findTrains(findTrainRequest).getTrains();
        System.out.println("Found trains: \n" + trains);
        List<FreeSeatsSummary> freeSeats = trains.stream()
                .filter(train -> train.getNumber().equals("079П"))
                .map(Train::getFreeSeats)
                .findFirst().get();
        Map<String, Integer> seatsByType = freeSeats.stream()
                .collect(Collectors.toMap(FreeSeatsSummary::getLetter, FreeSeatsSummary::getPlaces));
        System.out.println("Free seats: " + freeSeats);
        System.out.println("Free seats by type: " + seatsByType);
    }

}
