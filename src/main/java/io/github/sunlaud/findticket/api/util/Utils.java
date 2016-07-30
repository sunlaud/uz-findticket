package io.github.sunlaud.findticket.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Utils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public static void prettyPrintResponse(String rawResponse) {
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        HashMap<String,Object> o = OBJECT_MAPPER.readValue(rawResponse, typeRef);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MapUtils.debugPrint(new PrintStream(bos), "response", o);
        log.debug(bos.toString("UTF-8"));
    }

    @SneakyThrows
    public static String asUrlEncodedString(Object object) {
        String objectAsString = OBJECT_MAPPER.writeValueAsString(object);
        Map<?, ?> objectAsMap = OBJECT_MAPPER.readValue(objectAsString, Map.class);
        return objectAsMap.entrySet().stream()
                .map(e -> URLEncoder.encode(String.valueOf(e.getKey())) + "=" + URLEncoder.encode(String.valueOf(e.getValue())))
                .collect(Collectors.joining("&"));
    }
}
