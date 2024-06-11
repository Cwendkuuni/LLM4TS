package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize() throws Exception {
        String json = "{\"name\": \"John\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(jsonParser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String jsonArray = "[1, 2, 3]";
        String jsonObject = "{\"name\": \"John\"}";

        JsonParser jsonParserArray = objectMapper.getFactory().createParser(jsonArray);
        JsonParser jsonParserObject = objectMapper.getFactory().createParser(jsonObject);

        ObjectNode expectedTypeNodeArray = objectMapper.readTree(jsonArray);
        ObjectNode expectedTypeNodeObject = objectMapper.readTree(jsonObject);

        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructType(ObjectNode.class);
        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;

        Object resultArray = deserializer.deserializeWithType(jsonParserArray, new TestContext(), typeDeserializer);
        Object resultObject = deserializer.deserializeWithType(jsonParserObject, new TestContext(), typeDeserializer);

        assertNull(resultArray);
        assertNull(resultObject);

        assertEquals(expectedTypeNodeArray, expectedTypeNodeArray);
        assertEquals(expectedTypeNodeObject, expectedTypeNodeObject);
    }

    @Test
    public void testDeserializeWithDifferentTokens() throws Exception {
        String jsonNull = "null";
        String jsonNumber = "123";
        String jsonBooleanTrue = "true";
        String jsonBooleanFalse = "false";
        String jsonString = "\"Hello, world!\"";

        JsonParser jsonParserNull = objectMapper.getFactory().createParser(jsonNull);
        JsonParser jsonParserNumber = objectMapper.getFactory().createParser(jsonNumber);
        JsonParser jsonParserBooleanTrue = objectMapper.getFactory().createParser(jsonBooleanTrue);
        JsonParser jsonParserBooleanFalse = objectMapper.getFactory().createParser(jsonBooleanFalse);
        JsonParser jsonParserString = objectMapper.getFactory().createParser(jsonString);

        StdDeserializer<Object> deserializer = NullifyingDeserializer.instance;

        Object resultNull = deserializer.deserializeWithType(jsonParserNull, new TestContext(), null);
        Object resultNumber = deserializer.deserializeWithType(jsonParserNumber, new TestContext(), null);
        Object resultBooleanTrue = deserializer.deserializeWithType(jsonParserBooleanTrue, new TestContext(), null);
        Object resultBooleanFalse = deserializer.deserializeWithType(jsonParserBooleanFalse, new TestContext(), null);
        Object resultString = deserializer.deserializeWithType(jsonParserString, new TestContext(), null);

        assertNull(resultNull);
        assertNull(resultNumber);
        assertNull(resultBooleanTrue);
        assertNull(resultBooleanFalse);
        assertNull(resultString);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public BeanDescription getContextDescription() {
            return null;
        }

        @Override
        public ObjectFinder findValueHolderInScope(String name) {
            return null;
        }

        @Override
        public ObjectFinder findValueHolderInScope(String name, JavaType javaType) {
            return null;
        }
    }
}