package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.IOException;

/**
 * Needed coz {@link com.fasterxml.jackson.datatype.joda.deser.PeriodDeserializer}
 * doesn't support custom formats and standard params do not fit to UZ format "hours:minutes"
 */
public class CustomFormatPeriodDeserializer extends JsonDeserializer {
    private static final PeriodFormatter FORMATTER = new PeriodFormatterBuilder()
            .appendHours()
            .appendLiteral(":")
            .appendMinutes()
            .toFormatter();

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            if (str.isEmpty()) {
                return null;
            }
            return FORMATTER.parsePeriod(str);
        }
        throw ctxt.mappingException("Custom deserializer: " +
                "don't know how to deserialize value '%s' using hardcoded format (hours:minutes)");
    }
}
