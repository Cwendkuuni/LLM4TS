package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonNodeDeserializerTest {

    private JsonNodeFactory nodeFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        nodeFactory = mock(JsonNodeFactory.class);
        objectMapper = new ObjectMapper();
        objectMapper.setNodeFactory(nodeFactory);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.NULL);

        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(jsonParser, context);

        assertNull(result);
    }

    @Test
    public void testObjectDeserialization() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonNode result = objectDeserializer.deserialize(jsonParser, context);

        ObjectNode expected = nodeFactory.objectNode();
        assertEquals(expected, result);
    }

    @Test
    public void testArrayDeserialization() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonNode result = arrayDeserializer.deserialize(jsonParser, context);

        ArrayNode expected = nodeFactory.arrayNode();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfComplexJson() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"cars\":[{\"brand\":\"Ford\",\"model\":\"Mustang\"},{\"brand\":\"BMW\",\"model\":\"5 Series\"}]}";

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext context = mock(DeserializationContext.class);

        JsonNode result = JsonNodeDeserializer.instance.deserialize(jsonParser, context);

        ObjectNode expected = nodeFactory.objectNode();
        expected.put("name", nodeFactory.textNode("John"));
        expected.put("age", nodeFactory.numberNode(30));
        ArrayNode cars = expected.putArray("cars");
        ObjectNode car1 = cars.addObject();
        car1.put("brand", nodeFactory.textNode("Ford"));
        car1.put("model", nodeFactory.textNode("Mustang"));
        ObjectNode car2 = cars.addObject();
        car2.put("brand", nodeFactory.textNode("BMW"));
        car2.put("model", nodeFactory.textNode("5 Series"));

        assertEquals(expected, result);
    }
}