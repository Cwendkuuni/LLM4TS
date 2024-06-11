package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;

public class AsWrapperTypeDeserializerTest extends ObjectMapperTestBase {

    private static final String CLASS_NAME = AsWrapperTypeDeserializer.class.getName();
    private static final AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, String.class);

    @org.junit.Test
    public void testDeserializeTypedFromObject() throws Exception {
        String json = "{\"@type\":\"com.test.MyData\",\"data\":\"test\"}";
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue(json, deserializer.getType());
        assertEquals("test", result);
        assertTrue(result instanceof MyData);
    }

    @org.junit.Test
    public void testDeserializeTypedFromArray() throws Exception {
        String json = "[{\"@type\":\"com.test.MyData\",\"data\":\"test1\"},{\"@type\":\"com.test.MyData\",\"data\":\"test2\"}]";
        ObjectMapper mapper = new ObjectMapper();
        Object[] results = mapper.readValue(json, deserializer.getType().getComponentType());
        assertArrayEquals(new Object[]{new MyData("test1"), new MyData("test2")}, results);
    }

    @org.junit.Test
    public void testDeserializeTypedFromScalar() throws Exception {
        String json = "\"test\"";
        ObjectMapper mapper = new ObjectMapper();
        Object result = mapper.readValue(json, deserializer.getType());
        assertNull(result);
    }

    @org.junit.Test
    public void testDeserializeTypedFromAny() throws Exception {
        String json1 = "{\"@type\":\"com.test.MyData\",\"data\":\"test\"}";
        String json2 = "[1,2,3]";
        ObjectMapper mapper = new ObjectMapper();
        Object result1 = mapper.readValue(json1, deserializer.getType());
        Object result2 = mapper.readValue(json2, deserializer.getType());
        assertEquals("test", result1);
        assertTrue(result1 instanceof MyData);
        assertNull(result2);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"@class\":\"com.test.MyData\",\"data\":\"test\"}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper.DefaultTyping typeFinder = new ObjectMapper.DefaultTyping();
        Object result = mapper.readValue(json, deserializer.getType(), typeFinder);
        assertEquals("test", result);
        assertTrue(result instanceof MyData);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeInvalidObject() throws Exception {
        String json = "{\"@type\":\"com.test.MyData\",\"data\":\"test\"}"; // valid
        String invalidJson = "{\"@type\":\"com.test.MyData\"}"; // missing data
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, deserializer.getType()); // valid
        mapper.readValue(invalidJson, deserializer.getType()); // missing data should throw exception
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeInvalidArray() throws Exception {
        String json1 = "[{\"@type\":\"com.test.MyData\",\"data\":\"test1\"},{\"@type\":\"com.test.MyData\",\"data\":\"test2\"}]"; // valid
        String invalidJson = "[1,2,3]"; // invalid JSON array
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json1, deserializer.getType().getComponentType()); // valid
        mapper.readValue(invalidJson, deserializer.getType().getComponentType()); // invalid JSON array should throw exception
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeInvalidScalar() throws Exception {
        String json = "\"test\""; // valid
        String invalidJson = "test"; // invalid JSON scalar
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, deserializer.getType()); // valid
        mapper.readValue(invalidJson, deserializer.getType()); // invalid JSON scalar should throw exception
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeInvalidAny() throws Exception {
        String json1 = "{\"@type\":\"com.test.MyData\",\"data\":\"test\"}"; // valid
        String json2 = "{test:1,data:\"test\"}"; // invalid JSON object structure
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json1, deserializer.getType()); // valid
        mapper.readValue(json2, deserializer.getType()); // invalid JSON object structure should throw exception
    }

    @org.junit.Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<MyData> deserializer = mapper.findValueDeserializer(MyData.class);
        assertEquals(MyDataDeserializer.class, deserializer.getClass());
    }

    private static class MyDataDeserializer extends StdDeserializer<MyData> {
        public MyDataDeserializer() {
            this(null);
        }

        public MyDataDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MyData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String data = jp.getValueAsString();
            return new MyData(data);
        }
    }

    private static class MyData {
        private final String data;

        public MyData(String data) {
            this.data = data;
        }

        @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="@class")
        public static class MyDataType {}

        public String getData() {
            return data;
        }
    }
}