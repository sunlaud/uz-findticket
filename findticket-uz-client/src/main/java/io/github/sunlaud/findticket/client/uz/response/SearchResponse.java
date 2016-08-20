package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SearchResponse<T> {
    @JsonProperty("data")
    protected Object data;

    @JsonProperty("captcha")
    protected Object captcha;

    @JsonProperty("error")
    protected boolean error;

    public abstract T getValue();
}
