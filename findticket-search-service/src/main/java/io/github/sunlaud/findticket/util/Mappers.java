package io.github.sunlaud.findticket.util;

import io.github.sunlaud.findticket.client.uz.dto.FreeSeatsDto;
import io.github.sunlaud.findticket.client.uz.dto.StationDto;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.api.SeatsSummary;
import io.github.sunlaud.findticket.api.Station;
import io.github.sunlaud.findticket.api.Train;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.time.LocalDateTime;

public class Mappers {
    private static final Mappers INSTANCE = new Mappers();
    private final MapperFactory mapperFactory;

    public Mappers() {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(StationDto.class, Station.class)
                .byDefault()
                .field("title", "name")
                .register();
        mapperFactory.classMap(FreeSeatsDto.class, SeatsSummary.class)
                .byDefault()
                .register();
        mapperFactory.classMap(TrainDto.class, Train.class)
                .field("from.stationName", "stationFrom.name")
                .field("from.date", "departureDate")
                .field("till.stationName", "stationTill.name")
                .field("till.date", "arrivalDate")
                .field("from.stationName", "routeStartStationName")
                .field("till.stationName", "routeEndStationName")
                .exclude("travelTime")
                .byDefault()
                .customize(
                        new CustomMapper<TrainDto, Train>() {
                            public void mapAtoB(TrainDto src, Train target, MappingContext context) {
                                target.setTravelTime(src.getTravelTime());
                            }
                        })
                .register();

        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));

    }

    public static MapperFacade get() {
        return INSTANCE.mapperFactory.getMapperFacade();
    }
}
