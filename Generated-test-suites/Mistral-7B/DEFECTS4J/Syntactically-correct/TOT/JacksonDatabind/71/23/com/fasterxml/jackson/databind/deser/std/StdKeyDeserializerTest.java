package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private static final TypeFactory typeFactory = mapper.getTypeFactory();

    @Test
    public void testBooleanTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testByteTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("1", context);
        assertEquals((byte) 1, key);

        key = deserializer.deserializeKey("-127", context);
        assertEquals((byte) -127, key);

        key = deserializer.deserializeKey("256", context);
        assertNull(key);
    }

    @Test
    public void testShortTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("100", context);
        assertEquals((short) 100, key);

        key = deserializer.deserializeKey("-32768", context);
        assertEquals((short) -32768, key);

        key = deserializer.deserializeKey("32768", context);
        assertNull(key);
    }

    @Test
    public void testCharTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("A", context);
        assertEquals('A', key);

        key = deserializer.deserializeKey("a", context);
        assertEquals('a', key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testIntTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("1000", context);
        assertEquals(1000, key);

        key = deserializer.deserializeKey("-2147483648", context);
        assertEquals(-2147483648, key);

        key = deserializer.deserializeKey("2147483647", context);
        assertNull(key);
    }

    @Test
    public void testLongTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("10000000000L", context);
        assertEquals(10000000000L, key);

        key = deserializer.deserializeKey("-9223372036854775808L", context);
        assertEquals(-9223372036854775808L, key);

        key = deserializer.deserializeKey("9223372036854775807L", context);
        assertNull(key);
    }

    @Test
    public void testFloatTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("1.234", context);
        assertEquals(1.234f, key, 0.001f);

        key = deserializer.deserializeKey("NaN", context);
        assertEquals(Float.NaN, key);

        key = deserializer.deserializeKey("inf", context);
        assertEquals(Float.POSITIVE_INFINITY, key);

        key = deserializer.deserializeKey("-inf", context);
        assertEquals(Float.NEGATIVE_INFINITY, key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testDoubleTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("1.234", context);
        assertEquals(1.234, key, 0.001);

        key = deserializer.deserializeKey("NaN", context);
        assertEquals(Double.NaN, key);

        key = deserializer.deserializeKey("inf", context);
        assertEquals(Double.POSITIVE_INFINITY, key);

        key = deserializer.deserializeKey("-inf", context);
        assertEquals(Double.NEGATIVE_INFINITY, key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testLocaleTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext context = mapper.getDeserializationContext();
        context.setConfig(mapper.getSerializationConfig());
        context.setConfig(context.getConfig().withFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS, false));

        Object key = deserializer.deserializeKey("en_US", context);
        assertEquals(Locale.US, key);

        key = deserializer.deserializeKey("es_ES", context);
        assertEquals(Locale.SPAIN, key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testDateTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("2022-04-01", context);
        assertEquals(new Date(2022, 3, 1), key);

        key = deserializer.deserializeKey("2022-04-30", context);
        assertEquals(new Date(2022, 4, 30), key);

        key = deserializer.deserializeKey("2022-02-30", context);
        assertNull(key);
    }

    @Test
    public void testCalendarTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("2022-04-01", context);
        assertEquals(Calendar.getInstance().set(2022, 3, 1), key);

        key = deserializer.deserializeKey("2022-06-30", context);
        assertEquals(Calendar.getInstance().set(2022, 6, 30), key);

        key = deserializer.deserializeKey("2022-02-30", context);
        assertNull(key);
    }

    @Test
    public void testUUIDTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", context);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testURITypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), key);

        key = deserializer.deserializeKey("https://example.com", context);
        assertEquals(new URI("https://example.com"), key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testURLTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), key);

        key = deserializer.deserializeKey("https://example.com", context);
        assertEquals(new URL("https://example.com"), key);

        key = deserializer.deserializeKey("invalid", context);
        assertNull(key);
    }

    @Test
    public void testClassTypeKeyDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        DeserializationContext context = mapper.getDeserializationContext();
        Object key = deserializer.deserializeKey("java.lang.String", context);
        assertEquals(String.class, key);

        key = deserializer.deserializeKey("java.util.UUID", context);
        assertEquals(UUID.class, key);

        key = deserializer.deserializeKey("java.util.Date", context);
        assertEquals(Date.class, key);

        key = deserializer.deserializeKey("java.lang.Class", context);
        assertEquals(Class.class, key);

        key = deserializer.deserializeKey("java.util.Locale", context);
        assertEquals(Locale.class, key);

        key = deserializer.deserializeKey("java.util.Calendar", context);
        assertEquals(Calendar.class, key);

        key = deserializer.deserializeKey("java.net.URI", context);
        assertEquals(URI.class, key);

        key = deserializer.deserializeKey("java.net.URL", context);
        assertEquals(URL.class, key);

        key = deserializer.deserializeKey("com.example.MyCustomClass", context);
        assertNull(key);
    }
}