package io.github.sunlaud.findticket.util.mappers;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import io.github.sunlaud.findticket.api.SeatsSummary;
import io.github.sunlaud.findticket.api.TransportRoute;
import io.github.sunlaud.findticket.client.uz.dto.FreeSeatsDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;

import static fr.xebia.extras.selma.IgnoreMissing.DESTINATION;

//sadly, but Selma mapper doesn't play well well with lombok :(
@Mapper(withIgnoreMissing = DESTINATION,
        withCustomFields = {
                @Field({"from.date","departureDate"}),
                @Field({"till.date","arrivalDate"})
        },
        withIgnoreFields = {
                "io.github.sunlaud.findticket.api.TransportRoute.from",
                "io.github.sunlaud.findticket.api.TransportRoute.till"
        }
)
public interface TransportRouteMapper {
    TransportRoute fromDto(TrainDto dto);

    @Maps(withCustomFields = {
            @Field({"stationName", "name"})
    })
    SeatsSummary fromFreeSeatsDto(FreeSeatsDto dto);
}
