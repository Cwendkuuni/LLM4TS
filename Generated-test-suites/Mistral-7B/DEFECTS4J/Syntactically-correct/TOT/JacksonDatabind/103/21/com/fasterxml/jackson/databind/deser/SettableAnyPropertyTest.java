package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SettableAnyPropertyTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSettableAnyProperty() throws JsonParseException, IOException {
        // Test with basic types
        JavaType javaTypeInt = mapper.getTypeFactory().constructType(int.class);
        SettableAnyProperty settableAnyPropertyInt = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(int.class, "testInt"), null), // setter is null for this test
                null, javaTypeInt, null, null, null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_empty_BEANS, false);

        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("testInt", 123);

        Object result = objectMapper.readValue(jsonNode, SettableAnyProperty.class);
        assertNotNull(result);
        assertEquals(settableAnyPropertyInt, result);
        assertEquals(123, ((Number) result.getProperty().get(result)).intValue());

        // Test with custom deserializer
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        SettableAnyProperty settableAnyPropertyString = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(String.class, "testString"), null),
                null, javaTypeInt, null, stringDeserializer, null);

        jsonNode = mapper.createObjectNode();
        jsonNode.put("testString", "Test");

        result = objectMapper.readValue(jsonNode, SettableAnyProperty.class);
        assertNotNull(result);
        assertEquals(settableAnyPropertyString, result);
        assertEquals("Test", result.getProperty().get(result));

        // Test with type deserializer
        TypeDeserializer intArrayTypeDeserializer = mapper.getTypeDeserializerForArray(int[].class);
        SettableAnyProperty settableAnyPropertyIntArray = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(int[].class, "testIntArray"), null),
                null, javaTypeInt.getComponentType().getArrayType(), null, null, intArrayTypeDeserializer);

        jsonNode = mapper.createObjectNode();
        ObjectNode arrayNode = jsonNode.putObject("testIntArray");
        arrayNode.put("0", 1);
        arrayNode.put("1", 2);

        result = objectMapper.readValue(jsonNode, SettableAnyProperty.class);
        assertNotNull(result);
        assertEquals(settableAnyPropertyIntArray, result);
        int[] intArray = (int[]) result.getProperty().get(result);
        assertEquals(2, intArray.length);
        assertEquals(1, intArray[0]);
        assertEquals(2, intArray[1]);

        // Test with key deserializer
        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(key);
            }
        };
        SettableAnyProperty settableAnyPropertyWithKeyDeserializer = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(String.class, "testString"), null),
                null, javaTypeInt, keyDeserializer, null, null);

        jsonNode = mapper.createObjectNode();
        jsonNode.put(1, "Test");

        result = objectMapper.readValue(jsonNode, SettableAnyProperty.class);
        assertNotNull(result);
        assertEquals(settableAnyPropertyWithKeyDeserializer, result);
        assertEquals("Test", result.getProperty().get(result));

        // Test null value
        SettableAnyProperty settableAnyPropertyNull = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(int.class, "testNull"), null),
                null, javaTypeInt, null, null, null);

        jsonNode = mapper.createObjectNode();
        jsonNode.put("testNull", null);

        result = objectMapper.readValue(jsonNode, SettableAnyProperty.class);
        assertNotNull(result);
        assertEquals(settableAnyPropertyNull, result);
        assertNull(result.getProperty().get(result));

        // Test missing property name
        SettableAnyProperty settableAnyPropertyMissingName = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(int.class, "testMissingName"), null),
                null, javaTypeInt, null, null, null);

        jsonNode = mapper.createObjectNode();

        result = objectMapper.readValue(jsonNode, settableAnyPropertyMissingName.getClass());
        assertNotNull(result);
        assertEquals(settableAnyPropertyMissingName, result);
        assertNull(result.getProperty());
    }

    static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }
    }
}