package io.github.sunlaud.findticket.client.uz.service;

import io.github.sunlaud.findticket.client.uz.Apis;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.request.GetAvailableSeatsRequest;
import io.github.sunlaud.findticket.client.uz.request.GetCoachesRequest;
import io.github.sunlaud.findticket.client.uz.response.ApiResponse;
import io.github.sunlaud.findticket.client.uz.response.GetAvailableSeatsResponse;
import io.github.sunlaud.findticket.client.uz.response.GetCoachesResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

public interface UzTicketSearchService {
    @POST
    @Path(Apis.FIND_STATIONS_URL)
    ApiResponse<List<StationDto>> findStations(@PathParam("stationNameFirstLetters") String stationNameFirstLetters);

    @POST
    @Path(Apis.FIND_TRAINS_URL)
    ApiResponse<List<TrainDto>> findTrains(FindTrainRequest findTrainRequest);

    @POST
    @Path(Apis.GET_COACHES_URL)
    GetCoachesResponse getCoaches(GetCoachesRequest getCoachesRequest);


    @POST
    @Path(Apis.GET_FREE_SEATS_URL)
    GetAvailableSeatsResponse getFreeSeats(GetAvailableSeatsRequest getAvailableSeatsRequest);
}
