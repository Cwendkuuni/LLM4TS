package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer.
 */
public class StringArrayDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeSingleElementStringArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[\"Test\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
        assertArrayEquals(new String[]{"Test"}, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[" + Base64Variants.STANDARD.encodeBase64String(TestObject.class.getName()) + "]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.readValue(jsonParser, JsonDeserializer.class));
        String[] result = deserializer.deserialize(jsonParser, new TestDeserializationContext());
        assertArrayEquals(new TestObject[0], result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonArray() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("{ \"data\": [1, 2, 3] }");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jsonParser, new TestDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJsonValue() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("[\"Test\", null, 1]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jsonParser, new TestDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJsonType() throws IOException {
        JsonParser jsonParser = mapper.getFactory().createParser("{ \"data\": [1] }");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jsonParser, new TestDeserializationContext());
    }

    static class TestDeserializationContext extends DeserializationContext {
        @Override
        public void handleUnexpectedToken(JsonParser p, JsonToken t) throws IOException {
            // Do nothing
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(Class<?> deserializerType, PropertyName propertyName) {
            return null;
        }

        @Override
        public ObjectMapper getFactory() {
            return mapper;
        }

        @Override
        public Object findValueWithType(Class<?> valueClass, JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    static class TestObject {
    }
}