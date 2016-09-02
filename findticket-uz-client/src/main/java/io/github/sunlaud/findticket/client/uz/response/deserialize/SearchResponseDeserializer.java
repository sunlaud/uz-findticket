package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.sunlaud.findticket.client.uz.response.ErrorSearchResponse;
import io.github.sunlaud.findticket.client.uz.response.FindStationResponse;
import io.github.sunlaud.findticket.client.uz.response.FindTrainResponse;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;

import java.io.IOException;

/**
 * not working - causes stack overflow coz indirectly calls itself
 */
public class SearchResponseDeserializer extends StdDeserializer<SearchResponse> {
    protected SearchResponseDeserializer() {
        super(SearchResponse.class);
    }

    @Override
    public SearchResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode treeNode = jp.readValueAsTree();
        JsonParser parser = treeNode.traverse(jp.getCodec());

        if (treeNode.get("error").asToken() == JsonToken.VALUE_TRUE) {
            return parser.readValueAs(ErrorSearchResponse.class);
        } else {
            return treeNode.at("/value/0/num").isValueNode()
                    ? parser.readValueAs(FindTrainResponse.class)
                    : parser.readValueAs(FindStationResponse.class);
        }
    }
}

