package io.github.sunlaud.findticket;

import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.Iterators;
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
                if (days == 0) {
                    return Iterators.singletonIterator(dateTime);
                }
                if (direction == OffsetDirection.BACKWARD_AND_FORWARD) {
                    return backwardsAndForwardsDaysIterator(dateTime);
                }
                return (direction == OffsetDirection.FORWARD)
                        ? new DaysIterator(dateTime, days, 1)
                        : new DaysIterator(dateTime, days, -1);
            }
        };
    }

    private Iterator<LocalDateTime> backwardsAndForwardsDaysIterator(final LocalDateTime dateTime) {
        final DaysIterator forward = new DaysIterator(dateTime.plusDays(1), days -1, 1);
        final DaysIterator backward = new DaysIterator(dateTime.minusDays(1), days - 1, -1);
        return new Iterator<LocalDateTime>() {
            private Iterator<LocalDateTime> current;

            @Override
            public boolean hasNext() {
                return forward.hasNext() || backward.hasNext();
            }

            @Override
            public LocalDateTime next() {
                LocalDateTime result = (current == null) ? dateTime : current.next();
                current = (current == forward) ? backward : forward;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static class DaysIterator extends AbstractSequentialIterator<LocalDateTime> {
        private final int step;
        private int left;

        DaysIterator(LocalDateTime first, int countExceptFirst, int step) {
            super(first);
            this.left = countExceptFirst;
            this.step = step;
        }

        @Override
        protected LocalDateTime computeNext(LocalDateTime previous) {
            return left-- > 0 ? previous.plusDays(step) : null;
        }
    }
}
