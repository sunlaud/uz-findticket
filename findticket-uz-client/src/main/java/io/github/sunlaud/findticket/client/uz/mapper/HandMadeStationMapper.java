package io.github.sunlaud.findticket.client.uz.mapper;

import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;

/** Need this as Selma mapper doesn't play well with lombok :( */
public class HandMadeStationMapper implements StationMapper {
    @Override
    public Station fromDto(StationDto dto) {
        return new Station(dto.getName(), String.valueOf(dto.getId()));
    }
}
