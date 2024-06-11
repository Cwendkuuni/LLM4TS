package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("true", new TestObject());
        assertTrue(result instanceof Boolean);
        assertEquals(true, ((Boolean) result).booleanValue());

        result = deserializer.deserializeKey("false", new TestObject());
        assertTrue(result instanceof Boolean);
        assertEquals(false, ((Boolean) result).booleanValue());
    }

    @Test
    public void testByteDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object result = deserializer.deserializeKey("123", new TestObject());
        assertTrue(result instanceof Byte);
        assertEquals((byte) 123, ((Byte) result).byteValue());

        try {
            deserializer.deserializeKey("456", new TestObject());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testShortDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object result = deserializer.deserializeKey("123", new TestObject());
        assertTrue(result instanceof Short);
        assertEquals((short) 123, ((Short) result).shortValue());

        try {
            deserializer.deserializeKey("45678", new TestObject());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCharDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object result = deserializer.deserializeKey("A", new TestObject());
        assertTrue(result instanceof Character);
        assertEquals('A', ((Character) result).charValue());

        try {
            deserializer.deserializeKey("BC", new TestObject());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testIntDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object result = deserializer.deserializeKey("123", new TestObject());
        assertTrue(result instanceof Integer);
        assertEquals(123, ((Integer) result).intValue());
    }

    @Test
    public void testLongDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object result = deserializer.deserializeKey("12345678901234567890", new TestObject());
        assertTrue(result instanceof Long);
        assertEquals(12345678901234567890L, ((Long) result).longValue());
    }

    @Test
    public void testFloatDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object result = deserializer.deserializeKey("123.45", new TestObject());
        assertTrue(result instanceof Float);
        assertEquals(123.45f, ((Float) result).floatValue(), 0.001f);
    }

    @Test
    public void testDoubleDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object result = deserializer.deserializeKey("123.45", new TestObject());
        assertTrue(result instanceof Double);
        assertEquals(123.45, ((Double) result).doubleValue(), 0.001);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        Object result = deserializer.deserializeKey("en_US", new TestObject());
        assertTrue(result instanceof Locale);
        assertEquals(Locale.US, (Locale) result);
    }

    @Test
    public void testDateDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object result = deserializer.deserializeKey("2022-12-31", new TestObject());
        assertTrue(result instanceof Date);
    }

    @Test
    public void testCalendarDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        Object result = deserializer.deserializeKey("2022-12-31", new TestObject());
        assertTrue(result instanceof Calendar);
    }

    @Test
    public void testUriDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object result = deserializer.deserializeKey("http://example.com", new TestObject());
        assertTrue(result instanceof URI);
        assertEquals(new URI("http://example.com"), (URI) result);
    }

    @Test
    public void testUrlDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object result = deserializer.deserializeKey("http://example.com", new TestObject());
        assertTrue(result instanceof URL);
        assertEquals(new URL("http://example.com"), (URL) result);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        Object result = deserializer.deserializeKey("java.lang.String", new TestObject());
        assertTrue(result instanceof Class<?>);
        assertEquals(String.class, (Class<?>) result);
    }

    @Test
    public void testEnumDeserialization() throws Exception {
        enum TestEnum {
            VALUE_1, VALUE_2, VALUE_3
        }

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
        Object result = deserializer.deserializeKey("VALUE_2", new TestObject());
        assertTrue(result instanceof TestEnum);
        assertEquals(TestEnum.VALUE_2, (TestEnum) result);

        try {
            deserializer.deserializeKey("INVALID", new TestObject());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    private static class TestObject {
    }
}