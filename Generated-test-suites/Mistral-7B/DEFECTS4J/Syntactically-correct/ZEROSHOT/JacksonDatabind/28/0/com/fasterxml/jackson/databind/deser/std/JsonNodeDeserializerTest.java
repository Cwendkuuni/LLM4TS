package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonNodeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testNullDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());
        assertNull(result);
    }

    @Test
    public void testObjectDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":31}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = (ObjectNode) objectDeserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());
        JsonNode expected = mapper.readValue(new File("expected_object.json"), new TypeReference<JsonNode>() {});
        assertEquals(expected, result);
    }

    @Test
    public void testArrayDeserialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = (ArrayNode) arrayDeserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());
        JsonNode expected = mapper.readValue(new File("expected_array.json"), new TypeReference<JsonNode>() {});
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationAny() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":31}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());
        JsonNode expected = mapper.readValue(new File("expected_object.json"), new TypeReference<JsonNode>() {});
        assertEquals(expected, result);

        parser.nextToken(); // move to the next token
        result = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());
        expected = mapper.readValue(new File("expected_array.json"), JsonNode.class.getGenericType());
        assertTrue(expected.isArray());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":31.5}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());
    }
}