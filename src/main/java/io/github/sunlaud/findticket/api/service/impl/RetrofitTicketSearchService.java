package io.github.sunlaud.findticket.api.service.impl;

import io.github.sunlaud.findticket.api.model.Coach;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;
import io.github.sunlaud.findticket.api.response.GetAvailableSeatsResponse;
import io.github.sunlaud.findticket.api.response.GetCoachesResponse;
import io.github.sunlaud.findticket.api.service.TicketSearchService;

/**
 * Implementation of {@link io.github.sunlaud.findticket.api.service.TicketSearchService}
 * using Retrofit library
 */
public class RetrofitTicketSearchService implements TicketSearchService {
    @Override
    public FindStationResponse findStations(String stationNameFirstLetters) {
        return null;
    }

    @Override
    public FindTrainResponse findTrains(FindTrainRequest findTrainRequest) {
        return null;
    }

    @Override
    public GetCoachesResponse getCoachesByType(Train train, String coachType) {
        return null;
    }

    @Override
    public GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach) {
        return null;
    }
}
