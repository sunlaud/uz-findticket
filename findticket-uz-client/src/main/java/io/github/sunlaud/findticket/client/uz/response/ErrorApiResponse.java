package io.github.sunlaud.findticket.client.uz.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sunlaud.findticket.client.uz.exception.ApiInvocationException;
import lombok.Data;

@Data
public class ErrorApiResponse<T> extends ApiResponse<T> {
    @JsonProperty("value")
    private String message;

    @Override
    public T getValue() {
        throw new ApiInvocationException(message);
    }
}
