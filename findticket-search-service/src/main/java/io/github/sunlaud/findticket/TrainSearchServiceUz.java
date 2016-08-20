package io.github.sunlaud.findticket;

import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;
import io.github.sunlaud.findticket.client.uz.service.UzTicketSearchService;
import io.github.sunlaud.findticket.client.uz.service.impl.feign.FeignTicketSearchServiceBuilder;
import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.Train;
import io.github.sunlaud.findticket.util.Mappers;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class TrainSearchServiceUz {
    private final UzTicketSearchService service = FeignTicketSearchServiceBuilder.getTicketSearchService();

    public List<Station> findStations(String prefix) {
        SearchResponse<List<StationDto>> response = service.findStations(prefix);
        return Mappers.get().mapAsList(response.getValue(), Station.class);
    }

    public List<Train> findTrains(Station from, Station till, LocalDateTime departure) {
        FindTrainRequest request = FindTrainRequest.builder()
                .departureDate(departure.toLocalDate())
                .departureTime(departure.toLocalTime())
                .stationIdFrom(from.getId())
                .stationIdTill(till.getId())
                .build();
        SearchResponse<List<TrainDto>> response = service.findTrains(request);
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
