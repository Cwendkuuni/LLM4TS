package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, "_class", false, String.class);

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"value\":\"hello world\"}";
        Object result = deserializer.deserializeTypedFromObject(mapper.parse(json), new TestContext());
        assertEquals("hello world", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        String json = "[{\"typeId\":\"java.lang.String\",\"value\":\"hello world\"}]";
        Object result = deserializer.deserializeTypedFromArray(mapper.parse(json), new TestContext());
        assertEquals("hello world", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"value\":\"hello world\"}";
        Object result = deserializer.deserializeTypedFromScalar(mapper.parse(json), new TestContext());
        assertEquals("hello world", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\",\"value\":\"hello world\"}";
        Object result = deserializer.deserializeTypedFromAny(mapper.parse(json), new TestContext());
        assertEquals("hello world", result);
    }

    @Test(expected = IOException.class)
    public void testInvalidJsonObject() throws IOException {
        String json = "[{\"typeId\":\"java.lang.String\",\"value\":\"hello world\"}]"; // missing closing ']'
        deserializer.deserializeTypedFromArray(mapper.parse(json), new TestContext());
    }

    @Test(expected = IOException.class)
    public void testMissingTypeId() throws IOException {
        String json = "{\"value\":\"hello world\"}";
        deserializer.deserializeTypedFromObject(mapper.parse(json), new TestContext());
    }

    @Test(expected = IOException.class)
    public void testMissingValue() throws IOException {
        String json = "{\"typeId\":\"java.lang.String\"}";
        deserializer.deserializeTypedFromObject(mapper.parse(json), new TestContext());
    }

    @Test(expected = IOException.class)
    public void testInvalidTypeId() throws IOException {
        String json = "{\"typeId\":\"java.lang.InvalidTypeId\",\"value\":\"hello world\"}";
        deserializer.deserializeTypedFromObject(mapper.parse(json), new TestContext());
    }

    @Test
    public void testNullTypeId() throws IOException {
        String json = "{\"typeId\":null,\"value\":\"hello world\"}";
        try {
            deserializer.deserializeTypedFromObject(mapper.parse(json), new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEmptyTypeId() throws IOException {
        String json = "{\"typeId\":\"\",\"value\":\"hello world\"}";
        try {
            deserializer.deserializeTypedFromObject(mapper.parse(json), new TestContext());
            fail();
        } catch (IOException e) {
            // expected
        }
    }
}