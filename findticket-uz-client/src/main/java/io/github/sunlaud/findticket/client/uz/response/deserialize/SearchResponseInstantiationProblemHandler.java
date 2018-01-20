package io.github.sunlaud.findticket.client.uz.response.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.node.ValueNode;
import io.github.sunlaud.findticket.client.uz.response.ErrorSearchResponse;
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
        TreeNode errorNode = treeNode.get("error");
        Class<?> responseClass = (errorNode != null && errorNode.isValueNode() && ((ValueNode) errorNode).asBoolean(true))
                ? ErrorSearchResponse.class
                : FindTrainResponse.class;
        return parser.readValueAs(responseClass);
    }
}
