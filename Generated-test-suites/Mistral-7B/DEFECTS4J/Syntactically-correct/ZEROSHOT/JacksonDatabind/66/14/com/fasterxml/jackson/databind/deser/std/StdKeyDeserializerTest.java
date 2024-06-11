package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.KeyDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumSet;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("true", context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("false", context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("1", context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        result = keyDeserializer.deserializeKey("0", context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testByteDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("127", context);
        Assert.assertTrue(result instanceof Byte);
        Assert.assertEquals((byte) 127, result);

        result = keyDeserializer.deserializeKey("-128", context);
        Assert.assertTrue(result instanceof Byte);
        Assert.assertEquals((byte) -128, result);

        result = keyDeserializer.deserializeKey("256", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testShortDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("32767", context);
        Assert.assertTrue(result instanceof Short);
        Assert.assertEquals((short) 32767, result);

        result = keyDeserializer.deserializeKey("-32768", context);
        Assert.assertTrue(result instanceof Short);
        Assert.assertEquals((short) -32768, result);

        result = keyDeserializer.deserializeKey("65536", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testCharDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("A", context);
        Assert.assertTrue(result instanceof Character);
        Assert.assertEquals('A', result);

        result = keyDeserializer.deserializeKey("\\u00E9", context);
        Assert.assertTrue(result instanceof Character);
        Assert.assertEquals('\u00E9', result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testIntDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("123456", context);
        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(123456, result);

        result = keyDeserializer.deserializeKey("-123456", context);
        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(-123456, result);

        result = keyDeserializer.deserializeKey("2147483648", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testLongDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("9223372036854775807", context);
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(9223372036854775807L, result);

        result = keyDeserializer.deserializeKey("-9223372036854775808", context);
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(-9223372036854775808L, result);

        result = keyDeserializer.deserializeKey("18446744073709551616", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testFloatDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("3.14159265", context);
        Assert.assertTrue(result instanceof Float);
        Assert.assertEquals(3.14159265f, result, 0.0001f);

        result = keyDeserializer.deserializeKey("NaN", context);
        Assert.assertTrue(result instanceof Float);
        Assert.assertEquals(Float.NaN, result);

        result = keyDeserializer.deserializeKey("INF", context);
        Assert.assertTrue(result instanceof Float);
        Assert.assertEquals(Float.POSITIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("-INF", context);
        Assert.assertTrue(result instanceof Float);
        Assert.assertEquals(Float.NEGATIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testDoubleDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("3.14159265358979323846", context);
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(3.14159265358979323846, result, 0.0000000000000001);

        result = keyDeserializer.deserializeKey("NaN", context);
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(Double.NaN, result);

        result = keyDeserializer.deserializeKey("INF", context);
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("-INF", context);
        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testLocaleDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("en_US", context);
        Assert.assertTrue(result instanceof Locale);
        Assert.assertEquals(Locale.US, result);

        result = keyDeserializer.deserializeKey("fr_FR", context);
        Assert.assertTrue(result instanceof Locale);
        Assert.assertEquals(Locale.FRANCE, result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testDateDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("2021-12-31T23:59:59Z", context);
        Assert.assertTrue(result instanceof Date);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testCalendarDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("2021-12-31", context);
        Assert.assertTrue(result instanceof Calendar);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testUUIDDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("{1, b29d0f01-303f-4e9a-8e4b-77837e9120ca}", context);
        Assert.assertTrue(result instanceof UUID);
        Assert.assertEquals(new UUID(1, 0xb29d0f01L), result);

        result = keyDeserializer.deserializeKey("{0, 00000000-0000-0000-0000-000000000000}", context);
        Assert.assertTrue(result instanceof UUID);
        Assert.assertEquals(UUID.randomUUID(), result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testURIDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("http://example.com/path/to/resource", context);
        Assert.assertTrue(result instanceof URI);
        Assert.assertEquals(new URI("http://example.com/path/to/resource"), result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testURLDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("http://example.com/path/to/resource", context);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(new URL("http://example.com/path/to/resource"), result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testClassDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("[Ljava.lang.String;", context);
        Assert.assertTrue(result instanceof String[]);

        result = keyDeserializer.deserializeKey("[Ljava.util.List;", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    @Test
    public void testEnumDeserializer() throws Exception {
        EnumSet<DaysOfWeek> expectedEnumSet = EnumSet.allOf(DaysOfWeek.class);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DaysOfWeek[].class);
        DeserializationContext context = createContext();

        Object result = keyDeserializer.deserializeKey("[Lcom.fasterxml.jackson.databind.deser.std.DaysOfWeek;", context);
        Assert.assertTrue(result instanceof DaysOfWeek[]);
        Assert.assertArrayEquals(expectedEnumSet.toArray(new DaysOfWeek[0]), (DaysOfWeek[]) result);

        result = keyDeserializer.deserializeKey("[]", context);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("foo", context);
        Assert.assertNull(result);
    }

    private DeserializationContext createContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }

    enum DaysOfWeek {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }
}