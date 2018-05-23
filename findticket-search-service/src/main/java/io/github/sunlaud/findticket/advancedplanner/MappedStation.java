package io.github.sunlaud.findticket.advancedplanner;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class MappedStation {
    @Getter
    private final String stationName;
    private final double latitude;
    private final double longitude;

    public double distanceTo(MappedStation other) {
        double latitudeDiff = this.latitude - other.latitude;
        double longitudeDiff = this.longitude - other.longitude;
        return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
    }
}
