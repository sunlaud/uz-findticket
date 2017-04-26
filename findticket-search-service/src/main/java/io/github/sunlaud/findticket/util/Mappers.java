package io.github.sunlaud.findticket.util;

import fr.xebia.extras.selma.Selma;
import io.github.sunlaud.findticket.util.mappers.HandMadeStationMapper;
import io.github.sunlaud.findticket.util.mappers.HandMadeTransportRouteMapper;
import io.github.sunlaud.findticket.util.mappers.StationMapper;
import io.github.sunlaud.findticket.util.mappers.TransportRouteMapper;
import lombok.Getter;

public class Mappers {
    private static final boolean USE_SELMA = false; //for now Selma doesn't play well with lombok
    private static final Mappers INSTANCE = new Mappers();
    @Getter
    private final StationMapper stationMapper;
    @Getter
    private final TransportRouteMapper routeMapper;

    public Mappers() {
        if (USE_SELMA) {
            stationMapper = Selma.getMapper(StationMapper.class);
            routeMapper = Selma.getMapper(TransportRouteMapper.class);
        } else {
            stationMapper = new HandMadeStationMapper();
            routeMapper = new HandMadeTransportRouteMapper();
        }
    }
/*        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(StationDto.class, Station.class)
                .byDefault()
                .field("name", "name")
                .register();
        mapperFactory.classMap(FreeSeatsDto.class, SeatsSummary.class)
                .byDefault()
                .register();
        mapperFactory.classMap(TrainDto.class, TransportRoute.class)
                .field("from.stationName", "from.name")
                .field("from.date", "departureDate")
                .field("till.stationName", "till.name")
                .field("till.date", "arrivalDate")
                .exclude("travelTime")
                .byDefault()
                .customize(
                        new CustomMapper<TrainDto, TransportRoute>() {
                            public void mapAtoB(TrainDto src, TransportRoute target, MappingContext context) {
                                target.setName(src.getFrom().getStationName() + " - " + src.getTill().getStationName());
                                target.setTravelTime(src.getTravelTime());
                            }
                        })
                .register();

        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));

    }
*/

    public static Mappers get() {
        return INSTANCE;
    }
}
