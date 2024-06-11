package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNullifyingDeserializerWithJsonParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(jsonParser, deserializer);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeNullifyingDeserializerWithJsonParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(jsonParser, deserializer, JsonType.ARRAY_TYPE);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeNullifyingDeserializerWithJsonParserStartObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": \"value\"}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(jsonParser, deserializer, new TypeReference<Object>() {});
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeNullifyingDeserializerWithJsonParserStartArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("[1, 2, 3]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(jsonParser, deserializer, JsonType.ARRAY_TYPE);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeNullifyingDeserializerWithJsonParserFieldName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"name\": null, \"value\": 123}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(jsonParser, deserializer, new TypeReference<Object>() {});
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeNullifyingDeserializerWithJsonParserInvalidTokenId() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = mapper.readValue(jsonParser, deserializer, new TypeReference<Object>() {});
        assertNull(result);

        // This test case should throw an exception as the deserializeWithType method
        // has different behavior for JsonTokenId.ID_FIELD_NAME
        JsonParser jsonParserWithInvalidToken = mapper.getFactory().createParser("{\"name\": 123}");
        Object resultWithInvalidToken = mapper.readValue(jsonParserWithInvalidToken, deserializer, new TypeReference<Object>() {});
    }
}