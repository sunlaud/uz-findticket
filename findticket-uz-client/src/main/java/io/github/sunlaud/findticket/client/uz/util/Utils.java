package io.github.sunlaud.findticket.client.uz.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Utils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SneakyThrows
    public static void prettyPrintResponse(String rawResponse) {
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        HashMap<String,Object> o = OBJECT_MAPPER.readValue(rawResponse, typeRef);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        new PrintStream(bos).print(o);
//        it is unwise to keep commons-collections only for pretty-print
//        MapUtils.debugPrint(new PrintStream(bos), "response", o);
        log.debug(bos.toString("UTF-8"));
    }

    @SneakyThrows
    public static String asUrlEncodedString(Object object) {
        String objectAsString = OBJECT_MAPPER.writeValueAsString(object);
        Map<?, ?> objectAsMap = OBJECT_MAPPER.readValue(objectAsString, Map.class);

        return Joiner.on("&").join(Iterables.transform(objectAsMap.entrySet(), new Function<Map.Entry<?,?>, String>() {
            @Override
            public String apply(Map.Entry<?, ?> e) {
                return URLEncoder.encode(String.valueOf(e.getKey())) + "=" + URLEncoder.encode(String.valueOf(e.getValue()));
            }
        }));
    }
}
