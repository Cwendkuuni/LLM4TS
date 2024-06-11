package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = keyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.TRUE, key);

        key = keyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Boolean);
        assertEquals(Boolean.FALSE, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid boolean key.");
    }

    @Test
    public void testByteKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = keyDeserializer.deserializeKey("123", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) 123, key);

        key = keyDeserializer.deserializeKey("-128", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Byte);
        assertEquals((byte) -128, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid byte key.");
    }

    @Test
    public void testCharKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Character.class);
        Object key = keyDeserializer.deserializeKey("'A'", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Character);
        assertEquals('A', key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid char key.");
    }

    @Test
    public void testShortKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Short.class);
        Object key = keyDeserializer.deserializeKey("12345", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Short);
        assertEquals((short) 12345, key);

        key = keyDeserializer.deserializeKey("-32768", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Short);
        assertEquals((short) -32768, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid short key.");
    }

    @Test
    public void testIntKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = keyDeserializer.deserializeKey("123456", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Integer);
        assertEquals(123456, key);

        key = keyDeserializer.deserializeKey("-87654321", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Integer);
        assertEquals(-87654321, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid int key.");
    }

    @Test
    public void testLongKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object key = keyDeserializer.deserializeKey("12345678901234567890", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Long);
        assertEquals(12345678901234567890L, key);

        key = keyDeserializer.deserializeKey("-9223372036854775808", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Long);
        assertEquals(-9223372036854775808L, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid long key.");
    }

    @Test
    public void testFloatKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Float.class);
        Object key = keyDeserializer.deserializeKey("123.45", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Float);
        assertEquals(123.45f, key);

        key = keyDeserializer.deserializeKey("-123.45", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Float);
        assertEquals(-123.45f, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid float key.");
    }

    @Test
    public void testDoubleKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Double.class);
        Object key = keyDeserializer.deserializeKey("123.456789", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Double);
        assertEquals(123.456789, key);

        key = keyDeserializer.deserializeKey("-987654321.23", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Double);
        assertEquals(-987654321.23, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid double key.");
    }

    @Test
    public void testLocaleKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object key = keyDeserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.US, key);

        key = keyDeserializer.deserializeKey("fr_FR", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Locale);
        assertEquals(Locale.FRENCH, key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid locale key.");
    }

    @Test
    public void testDateKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);
        Object key = keyDeserializer.deserializeKey("2022-03-25", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Date);
        assertEquals(new Date(1647948800000L), key);

        key = keyDeserializer.deserializeKey("2022-03-25T12:34:56.789Z", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Date);
        assertEquals(new Date(1647967296789L), key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid date key.");
    }

    @Test
    public void testCalendarKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = keyDeserializer.deserializeKey("2022-03-25", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Calendar);
        Calendar calendar = (Calendar) key;
        assertEquals(Calendar.getInstance(), calendar);
        assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(2022, calendar.get(Calendar.YEAR));

        key = keyDeserializer.deserializeKey("2022-03-25T12:34:56.789Z", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Calendar);
        calendar = (Calendar) key;
        assertEquals(Calendar.getInstance(), calendar);
        assertEquals(Calendar.MARCH, calendar.get(Calendar.MONTH));
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.ZONE_OFFSET, calendar.getTimeZone().inZoneSameKindAs(calendar.getTimeZone()));

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid calendar key.");
    }

    @Test
    public void testUUIDKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = keyDeserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", objectMapper.getDeserializationContext());
        assertTrue(key instanceof UUID);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), key);

        key = keyDeserializer.deserializeKey("2.2.3.4-5.6.7.8-9.a.b.c-d.e.f.g-h.i.j.k", objectMapper.getDeserializationContext());
        assertTrue(key instanceof UUID);
        assertEquals(new UUID("2.2.3.4-5.6.7.8-9.a.b.c-d.e.f.g-h.i.j.k"), key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid UUID key.");
    }

    @Test
    public void testURIKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        Object key = keyDeserializer.deserializeKey("http://example.com/test", objectMapper.getDeserializationContext());
        assertTrue(key instanceof URI);
        assertEquals(new URI("http://example.com/test"), key);

        key = keyDeserializer.deserializeKey("https://example.com/test?param1=value1&param2=value2", objectMapper.getDeserializationContext());
        assertTrue(key instanceof URI);
        assertEquals(new URI("https://example.com/test?param1=value1&param2=value2"), key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid URI key.");
    }

    @Test
    public void testURLKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        Object key = keyDeserializer.deserializeKey("http://example.com/test", objectMapper.getDeserializationContext());
        assertTrue(key instanceof URL);
        assertEquals(new URL("http://example.com/test"), key);

        key = keyDeserializer.deserializeKey("https://example.com/test?param1=value1&param2=value2", objectMapper.getDeserializationContext());
        assertTrue(key instanceof URL);
        assertEquals(new URL("https://example.com/test?param1=value1&param2=value2"), key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid URL key.");
    }

    @Test
    public void testClassKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String[].class);
        Object key = keyDeserializer.deserializeKey("[Ljava.lang.String;@7f2589e2", objectMapper.getDeserializationContext());
        assertTrue(key instanceof String[]);
        String[] array = (String[]) key;
        assertEquals(new String[]{"element1", "element2"}, array);

        key = keyDeserializer.deserializeKey("[Ljava.lang.String;@3b6b7fa2", objectMapper.getDeserializationContext());
        assertTrue(key instanceof String[]);
        array = (String[]) key;
        assertEquals(new String[]{"element3", "element4"}, array);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid class key.");
    }

    @Test
    public void testCurrencyKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        Object key = keyDeserializer.deserializeKey("USD", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Currency);
        assertEquals(Currency.getInstance("USD"), key);

        key = keyDeserializer.deserializeKey("EUR", objectMapper.getDeserializationContext());
        assertTrue(key instanceof Currency);
        assertEquals(Currency.getInstance("EUR"), key);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid currency key.");
    }

    @Test
    public void testByteArrayKeyDeserializer() throws Exception {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte[].class);
        Object key = keyDeserializer.deserializeKey("RgVmYXVsdG9tbWl0aW9uIGlzIGh1bWFuLCBidXQgdG8gZnV0IGZvdWwgdG8gYW5kIHVwIHlvdSBzdGF0ZW1lbnQ=", objectMapper.getDeserializationContext());
        assertTrue(key instanceof byte[]);
        byte[] byteArray = (byte[]) key;
        byte[] expectedByteArray = {72, 111, 108, 111, 118, 101, 114, 116, 101, 103, 114, 111, 108, 100, 32, 76, 103, 116, 116, 112, 115, 111, 115, 101, 110, 32, 72, 101, 108, 108, 111};
        assertArrayEquals(expectedByteArray, byteArray);

        key = keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        assertNotNull(key);
        fail("Expected an exception for invalid byte array key.");
    }
}