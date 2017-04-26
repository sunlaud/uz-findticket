package io.github.sunlaud.findticket.util.mappers;

import fr.xebia.extras.selma.Mapper;
import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;

//sadly, but Selma mapper doesn't play well well with lombok :(
@Mapper
public interface StationMapper {
    Station fromDto(StationDto dto);
}
