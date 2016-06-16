package io.github.sunlaud.findticket.api.service;

import io.github.sunlaud.findticket.api.model.Coach;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;
import io.github.sunlaud.findticket.api.response.GetCoachesResponse;
import io.github.sunlaud.findticket.api.response.GetAvailableSeatsResponse;

public interface TicketSearchService {
    FindStationResponse findStations(String stationNameFirstLetters);
    FindTrainResponse findTrains(FindTrainRequest findTrainRequest);
    GetCoachesResponse getCoachesByType(Train train, String coachType);
    GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach);
}
