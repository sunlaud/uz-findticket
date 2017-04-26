package io.github.sunlaud.findticket.util.mappers;

import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;

/** Need this as Selma mapper doesn't play well with lombok :( */
public class HandMadeStationMapper implements StationMapper {
    @Override
    public Station fromDto(StationDto dto) {
        return new Station(dto.getName(), dto.getId());
    }
}
