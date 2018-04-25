package io.github.sunlaud.findticket;

import org.assertj.core.util.Lists;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DaysOffsetTest {


    @Test
    public void offsetsForward() {
        DaysOffset sut = new DaysOffset(3, OffsetDirection.FORWARD);
        LocalDateTime date = date("2042-12-29");

        Iterable<LocalDateTime> result = sut.applyTo(date);

        assertThat(Lists.newArrayList(result)).containsExactly(
                date("2042-12-29"),
                date("2042-12-30"),
                date("2042-12-31"),
                date("2043-01-01"));
    }

    @Test
    public void offsetsBackwards() {
        DaysOffset sut = new DaysOffset(3, OffsetDirection.BACKWARD);
        LocalDateTime date = date("2043-01-02");

        Iterable<LocalDateTime> result = sut.applyTo(date);

        assertThat(Lists.newArrayList(result)).containsExactly(
                date("2043-01-02"),
                date("2043-01-01"),
                date("2042-12-31"),
                date("2042-12-30"));
    }

    @Test
    public void offsetsBackwardsAndForwardsStartingFromInitialValueForwardFirst() {
        DaysOffset sut = new DaysOffset(3, OffsetDirection.BACKWARD_AND_FORWARD);
        LocalDateTime date = date("2043-01-01");

        Iterable<LocalDateTime> result = sut.applyTo(date);

        assertThat(Lists.newArrayList(result)).containsExactly(
                date("2043-01-01"),
                date("2043-01-02"),
                date("2042-12-31"),
                date("2043-01-03"),
                date("2042-12-30"),
                date("2043-01-04"),
                date("2042-12-29"));
    }

    private LocalDateTime date(String dateString) {
        return LocalDate.parse(dateString).toLocalDateTime(LocalTime.MIDNIGHT);
    }
}