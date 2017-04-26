package io.github.sunlaud.findticket;

import com.google.common.collect.Lists;
import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.TransportRoute;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;
import io.github.sunlaud.findticket.client.uz.service.UzTicketSearchService;
import io.github.sunlaud.findticket.client.uz.service.impl.feign.FeignTicketSearchServiceBuilder;
import io.github.sunlaud.findticket.util.Mappers;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class UzTrainRouteSearchService implements RouteSearchService {
    private final UzTicketSearchService api;

    public UzTrainRouteSearchService() {
        api = FeignTicketSearchServiceBuilder.getTicketSearchService();
    }

    @Override
    public List<Station> findStations(String nameSubstring) {
        List<StationDto> response = api.findStations(nameSubstring);
        return Lists.transform(response, Mappers.get().getStationMapper()::fromDto);
    }

    @Override
    public Iterable<TransportRoute> findRoutes(Station stationFrom, Station stationTo, LocalDateTime departureDate) {
        FindTrainRequest request = FindTrainRequest.builder()
                .departureDate(departureDate.toLocalDate())
                .departureTime(departureDate.toLocalTime())
                .stationIdFrom(stationFrom.getId())
                .stationIdTill(stationTo.getId())
                .build();

        SearchResponse<List<TrainDto>> response = api.findTrains(request);
        List<TransportRoute> routes = Lists.transform(response.getValue(), Mappers.get().getRouteMapper()::fromDto);
        routes.forEach(route -> {
            route.setFrom(stationFrom);
            route.setTill(stationTo);
        });
        return routes;
    }

//
//    @Override
//    public Map<String, List<CoachDto>> getCoaches(Train train) {
//        Map<String, List<CoachDto>> coaches = train.getFreeSeats().stream()
//                .map(SeatsSummary::getId)
//                .map(coachType -> getCoaches(train, coachType))
//                .flatMap(c -> c.getCoaches().stream())
//                .collect(Collectors.groupingBy(CoachDto::getType));
//        return coaches;
//    }
//
//    private GetCoachesResponse getCoaches(Train train, String coachType) {
//        GetCoachesRequest getCoachesRequest = GetCoachesRequest.builder()
//                .stationIdFrom(train.getFrom().getId())
//                .stationIdTill(train.getStationTill().getId())
//                .trainNumber(train.getId())
//                .departureDate(train.getDepartureDate())
//                .coachType(coachType)
//                .build();
//        return api.getCoaches(getCoachesRequest);
//    }
}
