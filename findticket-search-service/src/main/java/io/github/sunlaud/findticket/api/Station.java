package io.github.sunlaud.findticket.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Station {
    private String name;
    private int id;
}
