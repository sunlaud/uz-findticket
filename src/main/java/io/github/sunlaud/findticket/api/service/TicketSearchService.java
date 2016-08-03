package io.github.sunlaud.findticket.api.service;

import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.model.Station;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.ApiResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

public interface TicketSearchService {
    @POST
    @Path(Apis.FIND_STATIONS_URL)
    ApiResponse<List<Station>> findStations(@PathParam("stationNameFirstLetters") String stationNameFirstLetters);

    @POST
    @Path(Apis.FIND_TRAINS_URL)
    ApiResponse<List<Train>> findTrains(FindTrainRequest findTrainRequest);

//
//    @POST
//    @Path(Apis.GET_COACHES_URL)
//    GetCoachesResponse getCoachesByType(Train train, String coachType);
//
//    @POST
//    @Path(Apis.GET_FREE_SEATS_URL)
//    GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach);
}
