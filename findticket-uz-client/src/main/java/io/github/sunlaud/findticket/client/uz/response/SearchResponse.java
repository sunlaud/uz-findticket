package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class SearchResponse<T> {
    @JsonProperty("data")
    public Object data;

    @JsonProperty("captcha")
    public Object captcha;

    @JsonProperty("error")
    public boolean error;

    public abstract T getValue();
}
