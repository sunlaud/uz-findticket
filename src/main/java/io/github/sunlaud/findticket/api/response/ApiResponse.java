package io.github.sunlaud.findticket.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "error", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ErrorApiResponse.class, name = "true"),
        @JsonSubTypes.Type(value = FindTrainResponse.class, name = "null")
})
public abstract class ApiResponse<T> {
    @JsonProperty("data")
    protected Object data;

    @JsonProperty("captcha")
    protected Object captcha;

    @JsonProperty("error")
    protected boolean error;

    public abstract T getValue();
}
