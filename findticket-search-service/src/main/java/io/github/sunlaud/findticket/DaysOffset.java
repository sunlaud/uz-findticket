package io.github.sunlaud.findticket;

import lombok.Data;

@Data
public class DaysOffset {
    private final int days;
    private final OffsetDirection direction;
    private final int startDay;
    private final int endDay;

    public DaysOffset(int days, OffsetDirection direction) {
        this.days = days;
        this.direction = direction;
        this.startDay = (direction == OffsetDirection.FORWARD) ? 0 : -days;
        this.endDay = (direction == OffsetDirection.BACKWARD) ? 0 : days;
    }
}
