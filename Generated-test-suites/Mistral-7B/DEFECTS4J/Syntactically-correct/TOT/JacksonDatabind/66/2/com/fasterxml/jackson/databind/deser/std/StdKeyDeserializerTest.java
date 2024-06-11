package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.EnumResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanKeyDeserialization() throws IOException {
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
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testByteKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);

        String key = "127";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Byte);
        assertEquals((byte) 127, deserializedKey);

        key = "-128";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Byte);
        assertEquals((byte) -128, deserializedKey);

        key = "256";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testShortKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);

        String key = "32767";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Short);
        assertEquals((short) 32767, deserializedKey);

        key = "-32768";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Short);
        assertEquals((short) -32768, deserializedKey);

        key = "65536";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCharKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);

        String key = "A";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Character);
        assertEquals('A', deserializedKey);

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testIntKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);

        String key = "12345";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(12345, deserializedKey);

        key = "-12345";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Integer);
        assertEquals(-12345, deserializedKey);

        key = "Integer.MAX_VALUE";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLongKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);

        String key = "123456789012345";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Long);
        assertEquals(123456789012345L, deserializedKey);

        key = "-123456789012345";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Long);
        assertEquals(-123456789012345L, deserializedKey);

        key = "Long.MAX_VALUE";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testFloatKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);

        String key = "123.45";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Float);
        assertEquals(123.45f, deserializedKey, 0.01f);

        key = "-123.45";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Float);
        assertEquals(-123.45f, deserializedKey, 0.01f);

        key = "Float.MAX_VALUE";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDoubleKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);

        String key = "123.45";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Double);
        assertEquals(123.45, deserializedKey, 0.001);

        key = "-123.45";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Double);
        assertEquals(-123.45, deserializedKey, 0.001);

        key = "Double.MAX_VALUE";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testLocaleKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        String key = "en_US";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Locale);
        assertEquals(Locale.US, deserializedKey);

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testDateKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        String key = "2022-01-01";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Date);
        assertEquals("Sat Jan 01 00:00:00 CET 2022", deserializationTestHelper.formatDate(deserializedKey));

        key = "2022-02-30"; // invalid date
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testCalendarKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        String key = "2022-01-01";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Calendar);
        assertEquals(java.util.Calendar.getInstance().getTime(), deserializedKey.getTime());

        key = "2022-02-30"; // invalid date
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testUUIDKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);

        String key = "00000000-0000-0000-0000-000000000000";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof UUID);
        assertEquals(key, deserialializedKey.toString());

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testURILongFormKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        String key = "http://example.com/path?query=value#fragment";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof URI);
        assertEquals(key, deserializedKey.toString());

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testURLShortFormKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        String key = "http://example.com/path?query=value#fragment";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof URL);
        assertEquals(key, deserializedKey.toString());

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testClassKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        String key = "java.lang.String";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class<?>);
        assertEquals(String.class, deserializedKey);

        key = "java.util.Date";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class<?>);
        assertEquals(Date.class, deserializedKey);

        key = "com.example.MyClass";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class<?>);
        assertEquals(Class.forName("com.example.MyClass"), deserializedKey);

        key = "java.lang.Object";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof Class<?>);
        assertEquals(Object.class, deserializedKey);

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testEnumKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);

        String key = "MyEnum.ONE";
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof MyEnum);
        assertEquals(MyEnum.ONE, deserializedKey);

        key = "MyEnum.TWO";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof MyEnum);
        assertEquals(MyEnum.TWO, deserializedKey);

        key = "MyEnum.THREE";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertTrue(deserializedKey instanceof MyEnum);
        assertEquals(MyEnum.THREE, deserializedKey);

        key = "invalid";
        deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }

    @Test
    public void testNullKeyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Object.class);

        String key = null;
        Object deserializedKey = keyDeserializer.deserializeKey(key, mapper.getDeserializationContext());
        assertNull(deserializedKey);
    }
}