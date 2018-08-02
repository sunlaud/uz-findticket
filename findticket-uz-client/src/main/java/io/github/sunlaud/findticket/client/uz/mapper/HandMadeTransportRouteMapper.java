package io.github.sunlaud.findticket.client.uz.mapper;

import io.github.sunlaud.findticket.client.uz.dto.FreeSeatsDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.model.SeatType;
import io.github.sunlaud.findticket.model.TransportRoute;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/** Need this as Selma mapper doesn't play well with lombok :( */
public class HandMadeTransportRouteMapper implements TransportRouteMapper {

    @Override
    public TransportRoute fromDto(@NonNull TrainDto in) {
        TransportRoute out = new TransportRoute();
        out.setId(in.getId());
        out.setName(in.getName());
        out.setTravelTime(in.getTravelTime());
        out.setDepartureDate(in.getFrom().getDate());
        out.setArrivalDate(in.getTill().getDate());
        Map<SeatType, Integer> seats = new HashMap<>();
        for (FreeSeatsDto freeSeatsDto : in.getFreeSeats()) {
            SeatType seatType = new SeatType(freeSeatsDto.getId());
            seatType.setName(freeSeatsDto.getName());
            seats.put(seatType, freeSeatsDto.getPlaces());
        }
        out.setFreeSeatsCountByType(seats);
        out.setDetailsUrl(in.getDetailsUrl());
        return out;
    }
}
