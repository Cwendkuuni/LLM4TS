package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testSettableAnyProperty() throws IOException {
        // Create a test instance with a field setter
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "mapField"), null, null);
        AnnotatedMember setter = new AnnotatedField(MyPojo.class, "mapField");
        JavaType type = Map.class.getType();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        // Test deserialization and setting of a simple value
        ObjectMapper mapper = new ObjectMapper();
        MyPojo pojo = new MyPojo();
        String json = "{\"key\":\"value\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "mapField");
        Map<Object, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", "value");
        Map<Object, Object> actualMap = (Map<Object, Object>) settableAnyProperty.getProperty().get(pojo);
        assertEquals(expectedMap, actualMap);

        // Test deserialization and setting of a null value
        json = "{\"key\":null}";
        parser = mapper.getFactory().createParser(json);
        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "mapField");
        actualMap = (Map<Object, Object>) settableAnyProperty.getProperty().get(pojo);
        assertNull(actualMap.get("key"));

        // Test deserialization and setting of a complex value type
        TypeDeserializer valueTypeDeserializer = mapper.getTypeFactory().constructTypeDeserializer(ComplexType.class);
        settableAnyProperty = settableAnyProperty.withValueTypeDeserializer(mapper.readValue(json, ComplexType.class.getType()));
        ComplexType complexType = settableAnyProperty.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(complexType.getValue(), "complexValue");
        assertEquals(complexType.getOtherValue(), 42);

        // Test deserialization and setting of an empty JSON object
        json = "{}";
        parser = mapper.getFactory().createParser(json);
        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "mapField");
        actualMap = (Map<Object, Object>) settableAnyProperty.getProperty().get(pojo);
        assertNotNull(actualMap);
        assertTrue(actualMap.isEmpty());

        // Test deserialization and setting of a JSON object with multiple properties
        json = "{\"key1\":1,\"key2\":2,\"key3\":3}";
        parser = mapper.getFactory().createParser(json);
        settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "mapField");
        actualMap = (Map<Object, Object>) settableAnyProperty.getProperty().get(pojo);
        assertEquals(3, actualMap.size());
        assertEquals(1, actualMap.get("key1"));
        assertEquals(2, actualMap.get("key2"));
        assertEquals(3, actualMap.get("key3"));

        // Test error handling when deserializing an unknown type
        String unknownTypeJson = "{\"key\":{\"foo\":1}}";
        parser = mapper.getFactory().createParser(unknownTypeJson);
        try {
            settableAnyProperty.deserializeAndSet(parser, mapper.getDeserializationContext(), pojo, "mapField");
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    public static class MyPojo {
        private Map<Object, Object> mapField;

        public Map<Object, Object> getMapField() {
            return mapField;
        }
    }

    public static class ComplexType {
        private String value;
        private int otherValue;

        public ComplexType(String value, int otherValue) {
            this.value = value;
            this.otherValue = otherValue;
        }

        public String getValue() {
            return value;
        }

        public int getOtherValue() {
            return otherValue;
        }
    }
}