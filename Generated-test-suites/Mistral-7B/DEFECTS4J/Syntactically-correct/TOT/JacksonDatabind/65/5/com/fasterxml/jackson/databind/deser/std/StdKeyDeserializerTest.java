package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonAnnotationsTest
public class StdKeyDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testBooleanKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        Object key = serializer.deserializeKey("true", null);
        assertTrue((Boolean) key);

        key = serializer.deserializeKey("false", null);
        assertFalse((Boolean) key);

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testByteKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        Object key = serializer.deserializeKey("0", null);
        assertEquals((Byte) 0, key);

        key = serializer.deserializeKey("127", null);
        assertEquals((Byte) 127, key);

        key = serializer.deserializeKey("256", null);
        assertNull(key);
    }

    @Test
    public void testShortKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        Object key = serializer.deserializeKey("0", null);
        assertEquals((Short) 0, key);

        key = serializer.deserializeKey("Short.MAX_VALUE", null);
        assertEquals((Short) Short.MAX_VALUE, key);

        key = serializer.deserializeKey("-128", null);
        assertEquals((Short) -128, key);

        key = serializer.deserializeKey("123456", null);
        assertNull(key);
    }

    @Test
    public void testCharKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        Object key = serializer.deserializeKey("'A'", null);
        assertEquals((Character)'A', key);

        key = serializer.deserializeKey("'z'", null);
        assertEquals((Character)'z', key);

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testIntKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        Object key = serializer.deserializeKey("0", null);
        assertEquals((Integer) 0, key);

        key = serializer.deserializeKey("Integer.MAX_VALUE", null);
        assertEquals((Integer) Integer.MAX_VALUE, key);

        key = serializer.deserializeKey("-123456", null);
        assertEquals((Integer) -123456, key);

        key = serializer.deserializeKey("1234567890", null);
        assertNull(key);
    }

    @Test
    public void testLongKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        Object key = serializer.deserializeKey("0", null);
        assertEquals((Long) 0, key);

        key = serializer.deserializeKey("Long.MAX_VALUE", null);
        assertEquals((Long) Long.MAX_VALUE, key);

        key = serializer.deserializeKey("-123456789012345", null);
        assertEquals((Long) -123456789012345L, key);

        key = serializer.deserializeKey("12345678901234567890", null);
        assertNull(key);
    }

    @Test
    public void testFloatKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        Object key = serializer.deserializeKey("0.0", null);
        assertEquals((Float) 0.0f, key);

        key = serializer.deserializeKey("1.5", null);
        assertEquals((Float) 1.5f, key);

        key = serializer.deserializeKey("NaN", null);
        assertNull(key);

        key = serializer.deserializeKey("-Infinity", null);
        assertNull(key);
    }

    @Test
    public void testDoubleKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        Object key = serializer.deserializeKey("0.0", null);
        assertEquals((Double) 0.0, key);

        key = serializer.deserializeKey("1.5", null);
        assertEquals((Double) 1.5, key);

        key = serializer.deserializeKey("NaN", null);
        assertNull(key);

        key = serializer.deserializeKey("-Infinity", null);
        assertNull(key);
    }

    @Test
    public void testLocaleKeyDeserialization() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        Object key = serializer.deserializeKey("en_US", null);
        assertEquals(Locale.US, key);

        key = serializer.deserializeKey("fr_FR", null);
        assertEquals(Locale.FRANCE, key);

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testDateKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Date.class);

        Object key = serializer.deserializeKey("2019-12-31", null);
        assertEquals(new java.util.Date("2019-12-31"), key);

        key = serializer.deserializeKey("2018-02-29", null);
        assertNull(key);
    }

    @Test
    public void testCalendarKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(java.util.Calendar.class);

        Object key = serializer.deserializeKey("2019-12-31", null);
        java.util.Calendar calendar = (java.util.Calendar) key;
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(2019, calendar.get(Calendar.YEAR));

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testUUIDKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);

        Object key = serializer.deserializeKey("00000000-0000-0000-0000-000000000000", null);
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), key);

        key = serializer.deserializeKey("11111111-1111-1111-1111-111111111111", null);
        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), key);

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testURIKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        Object key = serializer.deserializeKey("http://example.com", null);
        assertEquals(new URI("http://example.com"), key);

        key = serializer.deserializeKey("https://example.com/path", null);
        assertEquals(new URI("https://example.com/path"), key);

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testURLKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        Object key = serializer.deserializeKey("http://example.com", null);
        assertEquals(new URL("http://example.com"), key);

        key = serializer.deserializeKey("https://example.com/path", null);
        assertEquals(new URL("https://example.com/path"), key);

        key = serializer.deserializeKey("invalid", null);
        assertNull(key);
    }

    @Test
    public void testClassKeyDeserialization() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);

        Object key = serializer.deserializeKey("java.lang.String", null);
        assertEquals(String.class, key);

        key = serializer.deserializeKey("java.util.HashMap", null);
        assertEquals(HashMap.class, key);

        key = serializer.deserializeKey("java.lang.Boolean", null);
        assertEquals(Boolean.class, key);

        key = serializer.deserializeKey("java.util.Date", null);
        assertEquals(Date.class, key);

        key = serializer.deserializeKey("java.net.URI", null);
        assertEquals(URI.class, key);

        key = serializer.deserializeKey("com.example.MyClass", null);
        assertEquals(Class.forName("com.example.MyClass"), key);

        key = serializer.deserializeKey("java.lang.Object", null);
        assertEquals(Object.class, key);

        key = serializer.deserializeKey("java.lang.NullPointerException", null);
        assertNull(key);
    }
}