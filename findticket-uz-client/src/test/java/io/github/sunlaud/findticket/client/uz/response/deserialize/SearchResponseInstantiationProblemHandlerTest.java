package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.client.uz.response.ErrorSearchResponse;
import io.github.sunlaud.findticket.client.uz.response.FindTrainResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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
                {"find_station.json", List.class},
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
        Object response = mapper.readValue(is, expectedClass);
        assertThat(response, instanceOf(expectedClass));


        boolean allFieldsAreNull = Iterables.all(Arrays.asList(response.getClass().getDeclaredFields()), Predicates.isNull());
        assertFalse("Deserialized object should have not null fields", allFieldsAreNull);
    }
}