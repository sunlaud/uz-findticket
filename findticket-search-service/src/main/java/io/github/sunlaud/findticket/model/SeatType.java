package io.github.sunlaud.findticket.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
public class SeatType {
    @NonNull
    private final String id;
    @NonNull @Setter
    private String name;

    public SeatType(String id) {
        this.id = id;
        this.name = id;
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }
}
