package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    // Test for boolean key deserialization
    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);

        String key = "true";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Boolean);
        assertEquals(Boolean.TRUE, deserializedKey);

        key = "false";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Boolean);
        assertEquals(Boolean.FALSE, deserializedKey);

        key = "invalid";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for Integer key deserialization
    @Test
    public void testIntegerKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        String key = "123";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(123, deserializedKey);

        key = "999999999";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(Integer.MAX_VALUE, deserializedKey);

        key = "-123";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(-123, deserializedKey);

        key = "invalid";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for String key deserialization
    @Test
    public void testStringKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        String key = "testString";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertEquals(key, deserializedKey);
    }

    // Test for UUID key deserialization
    @Test
    public void testUuidKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);

        String key = "123e4567-e89b-12d3-a456-426614174000";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof UUID);
        assertEquals(UUID.fromString(key), deserializedKey);
    }

    // Test for Enum key deserialization
    @JsonDeserialize(using = EnumKeyDeserializer.class)
    enum TestEnum {
        @JsonDeserialize(using = ToStringSerializer.class)
        ONE,
        TWO
    }

    @Test
    public void testEnumKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(TestEnum.class);

        String key = "ONE";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof TestEnum);
        assertEquals(TestEnum.ONE, deserializedKey);

        key = "InvalidEnum";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for Locale key deserialization
    @Test
    public void testLocaleKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        String key = "en_US";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Locale);
        assertEquals(Locale.US, deserializedKey);

        key = "InvalidLocale";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for Date key deserialization
    @Test
    public void testDateKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        String key = "2022-01-01";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Date);
        assertEquals("Mon Jan 03 00:00:00 CET 2022", mapper.writerWithRootValue(deserializedKey).writeValueAsString());

        key = "InvalidDate";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for Calendar key deserialization
    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        String key = "2022-01-01";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Calendar);
        assertEquals(1, ((Calendar) deserializedKey).get(Calendar.DAY_OF_MONTH));

        key = "InvalidDate";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for URL key deserialization
    @Test
    public void testUrlKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        String key = "https://www.example.com";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof URL);
        assertEquals("https://www.example.com", deserializedKey.toString());

        key = "InvalidURL";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for URI key deserialization
    @Test
    public void testUriKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        String key = "https://www.example.com";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof URI);
        assertEquals("https://www.example.com", deserializedKey.toString());

        key = "InvalidURI";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    // Test for Class key deserialization
    @Test
    public void testClassKeyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        String key = "java.lang.String";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class);
        assertEquals(String.class, deserializedKey);

        key = "com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class);
        assertEquals(getClass(), deserializedKey);

        key = "InvalidClass";
        try {
            deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }
}