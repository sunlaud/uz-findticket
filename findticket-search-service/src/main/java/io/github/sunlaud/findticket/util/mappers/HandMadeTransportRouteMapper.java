package io.github.sunlaud.findticket.util.mappers;

import io.github.sunlaud.findticket.api.SeatsSummary;
import io.github.sunlaud.findticket.api.TransportRoute;
import io.github.sunlaud.findticket.client.uz.dto.FreeSeatsDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

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
        List<SeatsSummary> seats = new ArrayList<>(in.getFreeSeats().size());
        for (FreeSeatsDto seat : in.getFreeSeats()) {
            seats.add(fromFreeSeatsDto(seat));
        }
        out.setFreeSeats(seats);
        return out;
    }

    @Override
    public SeatsSummary fromFreeSeatsDto(FreeSeatsDto in) {
        SeatsSummary out = new SeatsSummary();
        out.setId(in.getId());
        out.setName(in.getName());
        out.setPlaces(in.getPlaces());
        return out;
    }
}
