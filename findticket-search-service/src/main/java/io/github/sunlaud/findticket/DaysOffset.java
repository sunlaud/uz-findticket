package io.github.sunlaud.findticket;

import lombok.Data;
import org.joda.time.LocalDateTime;

import java.util.Iterator;

@Data
public class DaysOffset {
    public static final DaysOffset NO_OFFSET = new DaysOffset(0, OffsetDirection.FORWARD);
    private final int days;
    private final OffsetDirection direction;

    public DaysOffset(int days, OffsetDirection direction) {
        this.days = days;
        this.direction = direction;
    }

    public Iterable<LocalDateTime> applyTo(final LocalDateTime dateTime) {
        return new Iterable<LocalDateTime>() {
            @Override
            public Iterator<LocalDateTime> iterator() {
                final int startOffset = (direction == OffsetDirection.FORWARD) ? 0 : -days;
                final int endOffset = (direction == OffsetDirection.BACKWARD) ? 0 : days;

                return new Iterator<LocalDateTime>() {
                    private int currentOffset = startOffset;

                    @Override
                    public boolean hasNext() {
                        return currentOffset <= endOffset;
                    }

                    @Override
                    public LocalDateTime next() {
                        return dateTime.plusDays(currentOffset++);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

}
