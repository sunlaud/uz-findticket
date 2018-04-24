package io.github.sunlaud.findticket.client.uz.mapper;

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.model.TransportRoute;

import static fr.xebia.extras.selma.IgnoreMissing.DESTINATION;

//sadly, but Selma mapper doesn't play well well with lombok :(
@Mapper(withIgnoreMissing = DESTINATION,
        withCustomFields = {
                @Field({"from.date","departureDate"}),
                @Field({"to.date","arrivalDate"})
        },
        withIgnoreFields = {
                "io.github.sunlaud.findticket.model.TransportRoute.from",
                "io.github.sunlaud.findticket.model.TransportRoute.to"
        }
)
public interface TransportRouteMapper {
    TransportRoute fromDto(TrainDto dto);
}
