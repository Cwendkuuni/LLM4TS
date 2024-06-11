package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonAnnotationsModule
public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        context = mock(DeserializationContext.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testBooleanType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        Object result = deserializer.deserializeKey("true", context);
        assertEquals(Boolean.TRUE, result);

        result = deserializer.deserializeKey("false", context);
        assertEquals(Boolean.FALSE, result);

        String invalidKey = "invalid";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testByteType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = deserializer.deserializeKey("1", context);
        assertEquals((Byte) 1, result);

        String invalidKey = "256";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testShortType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        Object result = deserializer.deserializeKey("123", context);
        assertEquals((Short) 123, result);

        String invalidKey = "32000";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testCharType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        Object result = deserializer.deserializeKey("A", context);
        assertEquals((Character) 'A', result);

        String invalidKey = "AB";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testIntType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Object result = deserializer.deserializeKey("456", context);
        assertEquals((Integer) 456, result);

        String invalidKey = "1000000";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testLongType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        Object result = deserializer.deserializeKey("123456789012345", context);
        assertEquals((Long) 123456789012345L, result);

        String invalidKey = "123456789012346";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testFloatType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        Object result = deserializer.deserializeKey("1.23", context);
        assertEquals((Float) 1.23f, result);

        String invalidKey = "1.2345";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testDoubleType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Object result = deserializer.deserializeKey("1.23456789", context);
        assertEquals((Double) 1.23456789, result);

        String invalidKey = "1.234567891";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testLocaleType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = deserializer.deserializeKey("en_US", context);
        assertEquals(Locale.forLanguageTag("en-US"), result);

        String invalidKey = "en_US_Invalid";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testDateType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        Object result = deserializer.deserializeKey("2022-03-31", context);
        assertEquals(new Date(1648352000000L), result);

        String invalidKey = "2022-03-31T00:00:01";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testCalendarType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        Object result = deserializer.deserializeKey("2022-03-31", context);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DATE, 31);
        assertEquals(calendar, result);

        String invalidKey = "2022-03-31T00:00:01";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testUuidType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        Object result = deserializer.deserializeKey("123e4567-e89b-12d3-a456-426655440000", context);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426655440000"), result);

        String invalidKey = "123e4567-e89b-12d3-a456-426655440001";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testUriType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        Object result = deserializer.deserializeKey("http://example.com", context);
        assertEquals(new URI("http://example.com"), result);

        String invalidKey = "http://example.com/somepath?query=param#fragment";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testUrlType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        Object result = deserializer.deserializeKey("http://example.com", context);
        assertEquals(new URL("http://example.com"), result);

        String invalidKey = "http://example.com/somepath?query=param#fragment";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);
    }

    @Test
    public void testClassType() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);

        Object result = deserializer.deserializeKey("[Ljava.lang.String;", context);
        assertEquals(new String[] {}, result);

        result = deserializer.deserializeKey("[Ljava.lang.String;[Ljava.lang.String;", context);
        assertEquals(new String[][]{new String[]{}}, result);

        String invalidKey = "[Ljava.lang.String;[Ljava.lang.String;Ljava.lang.String;";
        result = deserializer.deserializeKey(invalidKey, context);
        assertNull(result);

        deserializer = StdKeyDeserializer.forType(EnumSet.class);

        EnumSet<TestEnum> expectedEnumSet = EnumSet.allOf(TestEnum.class);
        Object resultEnumSet = deserializer.deserializeKey(EnumSet.class.getName() + " [all]", context);
        assertEquals(expectedEnumSet, resultEnumSet);

        resultEnumSet = deserializer.deserializeKey(EnumSet.class.getName() + " [invalid]", context);
        assertNull(resultEnumSet);
    }
}

enum TestEnum {
    VALUE_1,
    VALUE_2,
    VALUE_3
}