package io.github.sunlaud.findticket.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;

public class Utils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public static void prettyPrintResponse(String rawResponse) {
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        HashMap<String,Object> o = OBJECT_MAPPER.readValue(rawResponse, typeRef);
        MapUtils.debugPrint(System.out, "response", o);
    }
}
