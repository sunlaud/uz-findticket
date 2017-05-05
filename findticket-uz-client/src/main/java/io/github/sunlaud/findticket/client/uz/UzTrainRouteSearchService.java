package io.github.sunlaud.findticket.client.uz;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.github.sunlaud.findticket.RouteSearchService;
import io.github.sunlaud.findticket.client.uz.client.UzTicketSearchClient;
import io.github.sunlaud.findticket.client.uz.client.impl.feign.FeignUzTicketSearchClientBuilder;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.mapper.Mappers;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UzTrainRouteSearchService implements RouteSearchService {
    private final UzTicketSearchClient api;

    public UzTrainRouteSearchService() {
        api = FeignUzTicketSearchClientBuilder.getTicketSearchService();
    }

    @Override
    public List<Station> findStations(String namePrefix) {
        List<StationDto> response = api.findStations(namePrefix);
        return Lists.transform(response, new Function<StationDto, Station>() {
            @Override
            public Station apply(StationDto dto) {
                return Mappers.get().getStationMapper().fromDto(dto);
            }
        });
    }

    @Override
    public Iterable<TransportRoute> findRoutes(Station stationFrom, Station stationTo, LocalDateTime departureDate) {
        FindTrainRequest request = FindTrainRequest.builder()
                .departureDate(departureDate.toLocalDate())
                .departureTime(departureDate.toLocalTime())
                .stationIdFrom(Integer.parseInt(stationFrom.getId()))
                .stationIdTill(Integer.parseInt(stationTo.getId()))
                .build();

        SearchResponse<List<TrainDto>> response = api.findTrains(request);
        List<TransportRoute> routes = new ArrayList<>(response.getValue().size());
        for (TrainDto trainDto : response.getValue()) {
            TransportRoute route = Mappers.get().getRouteMapper().fromDto(trainDto);
            route.setFrom(stationFrom);
            route.setTill(stationTo);
            routes.add(route);
        }
        return routes;
    }

//
//    @Override
//    public Map<String, List<CoachDto>> getCoaches(Train train) {
//        Map<String, List<CoachDto>> coaches = train.getFreeSeatsCountByType().stream()
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
//        return model.getCoaches(getCoachesRequest);
//    }
}
