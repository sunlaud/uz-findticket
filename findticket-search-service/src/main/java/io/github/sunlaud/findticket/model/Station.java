package io.github.sunlaud.findticket.model;

import lombok.Data;
import lombok.NonNull;


@Data
public class Station {
    @NonNull
    private final String name;
    @NonNull
    private final String id;

    @Override
    public String toString() {
        return "" + name + " (#" + id + ")";
    }
}
