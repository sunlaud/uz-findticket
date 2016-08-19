package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.TrainRoute;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.response.ApiResponse;
import io.github.sunlaud.findticket.client.uz.service.UzTicketSearchService;
import io.github.sunlaud.findticket.client.uz.service.impl.feign.FeignTicketSearchServiceBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TrainSearchServiceUz implements TrainSearchService {
    private final UzTicketSearchService api;

    public TrainSearchServiceUz() {
        api = FeignTicketSearchServiceBuilder.getTicketSearchService();
    }

    @Override
    public Iterable<TrainRoute> availableRoutes(String to, String from, LocalDate at) {
        FindTrainRequest request = FindTrainRequest.builder()
                .departureDate(at)
                .departureTime(LocalTime.of(0, 0))
//                .stationIdFrom(2210700)
                .stationIdFrom(Integer.parseInt(from))
//                .stationIdTill(2208001)
                .stationIdTill(Integer.parseInt(to))
                .build();

        ApiResponse<List<TrainDto>> response = api.findTrains(request);
        if (response.isError()) {
            System.out.println(response.getData());
            return Collections.emptyList();
        }

        return response.getValue().stream()
                .map(trainDto ->
                        TrainRoute.builder()
                                .trainID(trainDto.getNumber())
                                .fromStation(new Station(
                                        trainDto.getFrom().getStation(),
                                        String.valueOf(trainDto.getFrom().getStationId())
                                ))
                                .toStation(new Station(
                                        trainDto.getTill().getStation(),
                                        String.valueOf(trainDto.getTill().getStationId())
                                ))
                                .arriveAt(trainDto.getTill().getDate())
                                .departureAt(trainDto.getFrom().getDate())
                                .build())
                .collect(Collectors.toList());
    }
}
