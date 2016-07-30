package io.github.sunlaud.findticket.api.service;

import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface TicketSearchService {
    @POST
    @Path(Apis.FIND_STATIONS_URL)
    FindStationResponse findStations(@PathParam("stationNameFirstLetters") String stationNameFirstLetters);

    @POST
    @Path(Apis.FIND_TRAINS_URL)
    FindTrainResponse findTrains(FindTrainRequest findTrainRequest);

//
//    @POST
//    @Path(Apis.GET_COACHES_URL)
//    GetCoachesResponse getCoachesByType(Train train, String coachType);
//
//    @POST
//    @Path(Apis.GET_FREE_SEATS_URL)
//    GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach);
}
