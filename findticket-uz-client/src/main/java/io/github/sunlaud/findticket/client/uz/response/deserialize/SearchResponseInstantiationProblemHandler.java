package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import io.github.sunlaud.findticket.client.uz.response.ErrorSearchResponse;
import io.github.sunlaud.findticket.client.uz.response.FindStationResponse;
import io.github.sunlaud.findticket.client.uz.response.FindTrainResponse;
import io.github.sunlaud.findticket.client.uz.response.SearchResponse;

import java.io.IOException;

public class SearchResponseInstantiationProblemHandler extends DeserializationProblemHandler {
    @Override
    public Object handleMissingInstantiator(DeserializationContext ctxt, Class<?> instClass, JsonParser p, String msg) throws IOException {
        if (!SearchResponse.class.isAssignableFrom(instClass)) {
            return super.handleMissingInstantiator(ctxt, instClass, p, msg);
        }
        TreeNode treeNode = p.readValueAsTree();
        JsonParser parser = treeNode.traverse(p.getCodec());
        if (treeNode.get("error").asToken() == JsonToken.VALUE_TRUE) {
            return parser.readValueAs(ErrorSearchResponse.class);
        } else {
            return treeNode.at("/value/0/num").isValueNode()
                    ? parser.readValueAs(FindTrainResponse.class)
                    : parser.readValueAs(FindStationResponse.class);
        }
    }
}