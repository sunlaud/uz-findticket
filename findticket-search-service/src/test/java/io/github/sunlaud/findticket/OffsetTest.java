package io.github.sunlaud.findticket;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore("not implemented yet")
public class OffsetTest {


    @Test
    public void offsetsForward() {
        Offset<Integer> sut = new Offset<>(3, OffsetDirection.FORWARD);

        Iterable<Integer> result = sut.applyTo(-1);

        assertThat(result).containsExactly(-1, 0, 1, 2);
    }

    @Test
    public void offsetsBackwards() {
        Offset<Integer> sut = new Offset<>(3, OffsetDirection.BACKWARD);

        Iterable<Integer> result = sut.applyTo(2);

        assertThat(result).containsExactly(2, 1, 0, -1);
    }

    @Test
    public void offsetsBackwardsAndForwardsStartingFromInitialValueForwardFirst() {
        Offset<Integer> sut = new Offset<>(3, OffsetDirection.BACKWARD_AND_FORWARD);

        Iterable<Integer> result = sut.applyTo(2);

        assertThat(result).containsExactly(2, 3, 1, 4, 0, 5, -1);
    }
}