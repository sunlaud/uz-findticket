package io.github.sunlaud.findticket.api.service.impl;

import feign.Response;
import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


/**
 * Implementation of {@link io.github.sunlaud.findticket.api.service.TicketSearchService}
 * using Netfix Feign library
 */
public interface FeignTicketSearchService {
    @GET
    @Path("/")
    Response getRoot();

    @POST
    @Path(Apis.FIND_STATIONS_URL)
    FindStationResponse findStations(@PathParam("stationNameFirstLetters") String stationNameFirstLetters);

    @POST
    @Path(Apis.FIND_TRAINS_URL)
    FindTrainResponse findTrains(FindTrainRequest findTrainRequest);

    @POST
    @Path(Apis.FIND_TRAINS_URL)
    FindTrainResponse findTrains2(String findTrainRequest);
//
//    @POST
//    @Path(Apis.GET_COACHES_URL)
//    GetCoachesResponse getCoachesByType(Train train, String coachType);
//
//    @POST
//    @Path(Apis.GET_FREE_SEATS_URL)
//    GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach);
}
