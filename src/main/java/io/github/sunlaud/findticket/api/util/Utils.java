package io.github.sunlaud.findticket.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

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
}
