package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    @Test
    public void testTypeDeserializerBase() throws IOException {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(
                MAPPER.getTypeFactory().constructType(Object.class),
                new TestTypeIdResolver(), "typePropertyName", true,
                MAPPER.getTypeFactory().constructType(String.class)) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return new TestTypeDeserializer(prop);
            }
        };

        TestType type1 = new TestType("type1");
        TestType type2 = new TestType("type2");

        Map<String, JsonDeserializer<Object>> deserMap = deserializer._deserializers;

        // Test default deserializer
        Assert.assertEquals("default deserializer", (JsonDeserializer<Object>) null, deserMap.get("default"));

        // Test registering deserializer
        deserMap.put("type1", new TestJsonDeserializer1());
        JsonDeserializer<Object> deserializer1 = deserMap.get("type1");
        Assert.assertEquals("deserializer for type1", TestJsonDeserializer1.class, deserializer1.getClass());

        // Test deserialization with known type id
        String json1 = "{\"typeId\":\"type1\",\"value\":\"test\"}";
        TestType deser1 = MAPPER.readValue(new JsonFactory(), deserializer, json1);
        Assert.assertEquals("deserialized value with known type id", type1, deser1);

        // Test deserialization with missing type id
        String json2 = "{\"value\":\"test2\"}";
        TestType deser2;
        try {
            deser2 = MAPPER.readValue(new JsonFactory(), deserializer, json2);
            Assert.fail("expected JsonMappingException with missing type id");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test deserialization with unknown type id
        String json3 = "{\"typeId\":\"unknownTypeId\",\"value\":\"test3\"}";
        TestType deser3;
        try {
            deser3 = MAPPER.readValue(new JsonFactory(), deserializer, json3);
            Assert.fail("expected JsonMappingException with unknown type id");
        } catch (JsonMappingException e) {
            // expected
        }

        // Test deserialization with null input
        String json4 = "null";
        TestType deser4 = MAPPER.readValue(new JsonFactory(), deserializer, json4);
        Assert.assertEquals("deserialized value with null input", null, deser4);

        // Test deserialization with empty input
        String json5 = "{}";
        TestType deser5 = MAPPER.readValue(new JsonFactory(), deserializer, json5);
        Assert.assertEquals("deserialized value with empty input", null, deser5);

        // Test deserialization with type id as property name
        String json6 = "{\"typePropertyName\":\"type1\",\"value\":\"test6\"}";
        TestType deser6 = MAPPER.readValue(new JsonFactory(), deserializer, json6);
        Assert.assertEquals("deserialized value with type id as property name", type1, deser6);

        // Test deserialization with type id visible
        String json7 = "{\"typeId\":\"type1\",\"value\":\"test7\"}";
        TestType deser7 = MAPPER.readValue(new JsonFactory(), deserializer, json7);
        Assert.assertEquals("deserialized value with visible type id", type1, deser7);

        // Test deserialization with type id invisible
        String json8 = "{\"value\":\"test8\"}";
        TestType deser8;
        try {
            deser8 = MAPPER.readValue(new JsonFactory(), deserializer, json8);
            Assert.fail("expected JsonMappingException with invisible type id");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    private static class TestType {
        private final String name;

        public TestType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class TestTypeIdResolver
            extends TypeIdResolverBase<TypeDeserializerBase> {
        @Override
        public String idFromValue(Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof TestType) {
                return ((TestType) value).toString();
            }
            throw new IllegalArgumentException("Unknown value type: " + value.getClass());
        }

        @Override
        public String idFromValueAndType(Object value, JavaType type) {
            String id = idFromValue(value);
            if (id == null) {
                id = type.getName();
            }
            return id;
        }
    }

    private static class TestJsonDeserializer1
            extends JsonDeserializer<TestType> {
        @Override
        public TestType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestType result = new TestType("testJsonDeserializer1");
            ctxt.getCurrentRootValueInstance(TestType.class);
            return result;
        }
    }
}