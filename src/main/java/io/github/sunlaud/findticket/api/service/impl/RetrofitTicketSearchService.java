package io.github.sunlaud.findticket.api.service.impl;

import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.model.Coach;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;
import io.github.sunlaud.findticket.api.response.GetAvailableSeatsResponse;
import io.github.sunlaud.findticket.api.response.GetCoachesResponse;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Implementation of {@link io.github.sunlaud.findticket.api.service.TicketSearchService}
 * using Retrofit library
 */
public interface RetrofitTicketSearchService {
    @POST(Apis.FIND_STATIONS_URL)
    FindStationResponse findStations(@Path("stationNameFirstLetters") String stationNameFirstLetters);

    @FormUrlEncoded
    @POST(Apis.FIND_TRAINS_URL)
    FindTrainResponse findTrains(FindTrainRequest findTrainRequest);

    @POST(Apis.GET_COACHES_URL)
    GetCoachesResponse getCoachesByType(Train train, String coachType);

    @POST(Apis.GET_FREE_SEATS_URL)
    GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach);
}
