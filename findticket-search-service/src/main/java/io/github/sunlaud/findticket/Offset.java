package io.github.sunlaud.findticket;

import com.google.common.collect.Iterators;
import lombok.Data;

import java.util.Iterator;

@Data
public class Offset<T> {
    public static final Offset<?> NO_OFFSET = new Offset<>(0, OffsetDirection.FORWARD);
    private final int value;
    private final OffsetDirection direction;

    public Offset(int value, OffsetDirection direction) {
        this.value = value;
        this.direction = direction;
    }

    public Iterable<T> applyTo(final T start) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                if (value == 0) {
                    return Iterators.singletonIterator(start);
                }
                throw new RuntimeException("not implemented yet");
            }
        };
    }


    public static <P> Offset<P> noOffset() {
        return (Offset<P>) NO_OFFSET;
    }

}
