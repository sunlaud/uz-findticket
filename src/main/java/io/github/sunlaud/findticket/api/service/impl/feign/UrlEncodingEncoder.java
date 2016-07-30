package io.github.sunlaud.findticket.api.service.impl.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Arrays;

public class UrlEncodingEncoder implements Encoder {

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(JsonProperty.class))
                .map(field -> fieldToEncodedKeyValuePair(field, object))
                .forEach(kv -> sb.append(kv).append("&"));
        sb.deleteCharAt(sb.length() - 1);
        template.body(sb.toString());
    }


    @SneakyThrows
    private String fieldToEncodedKeyValuePair(Field field, Object obj) {
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value == null) {
            return "";
        }
        String key = field.getAnnotation(JsonProperty.class).value();
        return URLEncoder.encode(key) + "=" + URLEncoder.encode(value.toString());
    }
}
