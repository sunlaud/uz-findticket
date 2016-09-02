package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import io.github.sunlaud.findticket.client.uz.response.ErrorSearchResponse;
import io.github.sunlaud.findticket.client.uz.response.FindStationResponse;
import io.github.sunlaud.findticket.client.uz.response.FindTrainResponse;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class SearchResponseInstantiationProblemHandlerTest {
    private String jsonFilename;
    private Class<?> expectedClass;

    @Parameterized.Parameters(name= "content of <{0}> should deserialize into <{1}>")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"find_train.json", FindTrainResponse.class},
                {"find_station.json", FindStationResponse.class},
                {"error.json", ErrorSearchResponse.class}
        });
    }

    public SearchResponseInstantiationProblemHandlerTest(String jsonFilename, Class<?> expectedClass) {
        this.jsonFilename = jsonFilename;
        this.expectedClass = expectedClass;
    }

    @Test
    public void testDeserialize() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(jsonFilename);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationProblemHandler handler = new SearchResponseInstantiationProblemHandler();
        mapper.setConfig(mapper.getDeserializationConfig().withHandler(handler));
        SearchResponse response = mapper.readValue(is, SearchResponse.class);
        assertThat(response, instanceOf(expectedClass));
        boolean allFieldsAreNull = Arrays.stream(response.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(JsonProperty.class))
                .allMatch(Objects::isNull);
        assertFalse("Deserialized object should have not null fields", allFieldsAreNull);
    }
}