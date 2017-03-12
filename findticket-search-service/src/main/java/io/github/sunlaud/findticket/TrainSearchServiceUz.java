package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.Train;
import io.github.sunlaud.findticket.api.TrainRoute;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;
import io.github.sunlaud.findticket.client.uz.service.UzTicketSearchService;
import io.github.sunlaud.findticket.client.uz.service.impl.feign.FeignTicketSearchServiceBuilder;
import io.github.sunlaud.findticket.util.Mappers;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        SearchResponse<List<TrainDto>> response = api.findTrains(request);
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
                                        trainDto.getFrom().getStationId()
                                ))
                                .toStation(new Station(
                                        trainDto.getTill().getStation(),
                                        trainDto.getTill().getStationId()
                                ))
                                .arriveAt(trainDto.getTill().getDate())
                                .departureAt(trainDto.getFrom().getDate())
                                .build())
                .collect(Collectors.toList());
    }




    @Override
    public List<Station> findStations(String prefix) {
        List<StationDto> response = api.findStations(prefix);
        return Mappers.get().mapAsList(response, Station.class);
    }

    @Override
    public List<Train> findTrains(Station from, Station till, LocalDateTime departure) {
        FindTrainRequest request = FindTrainRequest.builder()
                .departureDate(departure.toLocalDate())
                .departureTime(departure.toLocalTime())
                .stationIdFrom(from.getId())
                .stationIdTill(till.getId())
                .build();
        SearchResponse<List<TrainDto>> response = api.findTrains(request);
        return Mappers.get().mapAsList(response.getValue(), Train.class);
    }


//                            Map<String, List<CoachDto>> coaches = train.getFreeSeats().stream()
//                                    .map(FreeSeatsDto::getLetter)
//                                    .map(coachType -> getCoaches(service, train, coachType))
//                                    .flatMap(c -> c.getCoaches().stream())
//                                    .collect(Collectors.groupingBy(CoachDto::getType));
/*
    private GetCoachesResponse getCoaches(UzTrainSearchClient service, TrainDto train, String coachType) {
        GetCoachesRequest getCoachesRequest = GetCoachesRequest.builder()
                .stationIdFrom(train.getFrom().getStationId())
                .stationIdTill(train.getTill().getStationId())
                .trainNumber(train.getNumber())
                .departureDateAsEpochSecond(train.getFrom().getDateAsEpochSecond())
                .coachType(coachType)
                .build();
        return service.getCoaches(getCoachesRequest);
    }
*/

}
