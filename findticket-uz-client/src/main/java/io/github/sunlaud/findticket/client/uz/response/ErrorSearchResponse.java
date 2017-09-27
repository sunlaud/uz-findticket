package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.uz.exception.ApiInvocationException;
import lombok.Data;

@Data
public class ErrorSearchResponse<T> extends SearchResponse<T> {
    @JsonProperty("value")
    private String message;

    @JsonIgnore
    @Override
    public T getValue() {
        throw new ApiInvocationException(message);
    }
}
