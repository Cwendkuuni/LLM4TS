package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.TestUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBooleanType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = deserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertTrue((Boolean) key);

        key = deserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertFalse((Boolean) key);
    }

    @Test
    public void testByteType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        Object key = deserializer.deserializeKey("127", objectMapper.getDeserializationContext());
        assertEquals((Byte) (byte) 127, key);

        try {
            key = deserializer.deserializeKey("128", objectMapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testShortType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        Object key = deserializer.deserializeKey("12345", objectMapper.getDeserializationContext());
        assertEquals((Short) 12345, key);

        try {
            key = deserializer.deserializeKey("12346", objectMapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCharType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        Object key = deserializer.deserializeKey("A", objectMapper.getDeserializationContext());
        assertEquals((Character) 'A', key);
    }

    @Test
    public void testIntType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        Object key = deserializer.deserializeKey("1234567890", objectMapper.getDeserializationContext());
        assertEquals((Integer) 1234567890, key);
    }

    @Test
    public void testLongType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        Object key = deserializer.deserializeKey("12345678901234567890", objectMapper.getDeserializationContext());
        assertEquals((Long) 12345678901234567890L, key);
    }

    @Test
    public void testFloatType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        Object key = deserializer.deserializeKey("123.45", objectMapper.getDeserializationContext());
        assertEquals((Float) 123.45f, key);
    }

    @Test
    public void testDoubleType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        Object key = deserializer.deserializeKey("123.456.789", objectMapper.getDeserializationContext());
        assertEquals((Double) 123.456789, key);
    }

    @Test
    public void testLocaleType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.DefaultTyping typings = objectMapper.getDefaultTyping();
        ObjectMapper.DefaultTyping originalTypings = null;
        try {
            originalTypings = objectMapper.getDefaultTyping();
            objectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_MAP);

            KeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
            Object key = deserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
            assertEquals(Locale.US, key);

            key = deserializer.deserializeKey("fr_FR", objectMapper.getDeserializationContext());
            assertEquals(Locale.FRANCE, key);
        } finally {
            objectMapper.setDefaultTyping(originalTypings);
        }
    }

    @Test
    public void testDateType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        Object key = deserializer.deserializeKey("2022-03-01", objectMapper.getDeserializationContext());
        assertEquals(TestUtils.parseDate("2022-03-01"), key);
    }

    @Test
    public void testCalendarType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.DefaultTyping typings = objectMapper.getDefaultTyping();
        ObjectMapper.DefaultTyping originalTypings = null;
        try {
            originalTypings = objectMapper.getDefaultTyping();
            objectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_MAP);

            KeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
            Object key = deserializer.deserializeKey("2022-03-01", objectMapper.getDeserializationContext());
            assertEquals(TestUtils.parseCalendar("2022-03-01"), key);
        } finally {
            objectMapper.setDefaultTyping(originalTypings);
        }
    }

    @Test
    public void testUUIDType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        Object key = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", objectMapper.getDeserializationContext());
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), key);
    }

    @Test
    public void testURIType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        Object key = deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        assertEquals(new URI("http://example.com"), key);
    }

    @Test
    public void testURLType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        Object key = deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        assertEquals(new URL("http://example.com"), key);
    }

    @Test
    public void testClassType() throws IOException {
        KeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);
        Object key = deserializer.deserializeKey("[Ljava.lang.String;", objectMapper.getDeserializationContext());
        assertEquals(new String[] {}, key);

        deserializer = StdKeyDeserializer.forType(String[].class);
        key = deserializer.deserializeKey("[Ljava.lang.String;[Ljava.lang.String;", objectMapper.getDeserializationContext());
        assertEquals(new String[][] {}, key);

        deserializer = StdKeyDeserializer.forType(String[].class);
        key = deserializer.deserializeKey("[Ljava.lang.String;[Ljava.lang.String;[Ljava.lang.String;]", objectMapper.getDeserializationContext());
        assertEquals(new String[][][] {}, key);
    }

    @Test
    public void testEnumType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper.DefaultTyping typings = objectMapper.getDefaultTyping();
        ObjectMapper.DefaultTyping originalTypings = null;
        try {
            originalTypings = objectMapper.getDefaultTyping();
            objectMapper.setDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_MAP);

            EnumResolver enumResolver = objectMapper.getConfiguration().getEnumResolver();
            AnnotatedMethod factoryMethod = ClassUtil.findMethod(Locale.class, "forLanguageTag", String.class);
            KeyDeserializer deserializer = new StdKeyDeserializer.EnumKD(enumResolver, factoryMethod);
            Object key = deserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
            assertEquals(Locale.US, key);
        } finally {
            objectMapper.setDefaultTyping(originalTypings);
        }
    }
}