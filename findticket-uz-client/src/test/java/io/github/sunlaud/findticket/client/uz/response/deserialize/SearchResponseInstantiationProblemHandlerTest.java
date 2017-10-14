package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import io.github.sunlaud.findticket.client.uz.response.ErrorSearchResponse;
import io.github.sunlaud.findticket.client.uz.response.FindTrainResponse;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SearchResponseInstantiationProblemHandlerTest {
    private String jsonFilename;
    private Class<?> expectedClass;

    @Parameterized.Parameters(name= "content of <{0}> should deserialize into <{1}>")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"find_train.json", FindTrainResponse.class},
                {"error.json", ErrorSearchResponse.class}
        });
    }

    public SearchResponseInstantiationProblemHandlerTest(String jsonFilename, Class<? extends SearchResponse> expectedClass) {
        this.jsonFilename = jsonFilename;
        this.expectedClass = expectedClass;
    }

    @Test
    public void testDeserialize() throws IOException {
        //GIVEN
        InputStream is = getClass().getClassLoader().getResourceAsStream(jsonFilename);

        //WHEN
        ObjectMapper mapper = new ObjectMapper();
        DeserializationProblemHandler handler = new SearchResponseInstantiationProblemHandler();
        mapper.setConfig(mapper.getDeserializationConfig().withHandler(handler));
        final SearchResponse response = mapper.readValue(is, SearchResponse.class);

        //THEN
        assertThat(response, instanceOf(expectedClass));

        boolean allFieldsAreNotNull = Iterables.all(Arrays.asList(response.getClass().getDeclaredFields()),
                new Predicate<Field>() {
                    @SneakyThrows
                    @Override
                    public boolean apply(Field field) {
                        field.setAccessible(true);
                        return field.get(response) != null;
                    }
                });
        assertTrue("Deserialized object should have not null fields", allFieldsAreNotNull);
    }
}