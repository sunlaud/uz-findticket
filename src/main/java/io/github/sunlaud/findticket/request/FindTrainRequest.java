package io.github.sunlaud.findticket.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindTrainRequest {
    private final Integer stationIdFrom;
    private final Integer stationIdTill;
    private final String departureTime;
    private final String departureDate;
}
