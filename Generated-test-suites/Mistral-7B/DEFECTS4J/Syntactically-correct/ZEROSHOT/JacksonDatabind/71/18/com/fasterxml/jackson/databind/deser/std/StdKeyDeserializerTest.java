package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Dynamic;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.TestUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

/**
 * Unit tests for {@link StdKeyDeserializer} class.
 */
@JacksonAnnotationsInTest
public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testForTypeString() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertEquals("String", deserializer.getKeyClass().getName());
        assertEquals("String", deserializer.deserializeKey("abc", mapper.getDeserializationContext()).toString());
    }

    @Test
    public void testForTypeBoolean() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals("Boolean", deserializer.getKeyClass().getName());
        assertTrue(deserializer.deserializeKey("true", mapper.getDeserializationContext()) instanceof Boolean);
        assertTrue(deserializer.deserializeKey("true", mapper.getDeserializationContext()).booleanValue());
        assertFalse(deserializer.deserializeKey("false", mapper.getDeserializationContext()) instanceof Boolean);
        assertFalse(deserializer.deserializeKey("false", mapper.getDeserializationContext()).booleanValue());
    }

    @Test
    public void testForTypeByte() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals("Byte", deserializer.getKeyClass().getName());
        assertEquals((Byte) Byte.parseByte("10"), deserializer.deserializeKey("10", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeShort() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals("Short", deserializer.getKeyClass().getName());
        assertEquals((Short) Short.parseShort("100"), deserializer.deserializeKey("100", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeChar() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals("Character", deserializer.getKeyClass().getName());
        assertEquals((Character) Character.forDigit('A', 10), deserializer.deserializeKey("A", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeInt() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals("Integer", deserializer.getKeyClass().getName());
        assertEquals((Integer) Integer.parseInt("123"), deserializer.deserializeKey("123", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeLong() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals("Long", deserializer.getKeyClass().getName());
        assertEquals((Long) Long.parseLong("456"), deserializer.deserializeKey("456", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeFloat() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals("Float", deserializer.getKeyClass().getName());
        assertEquals((Float) Float.parseFloat("123.45"), deserializer.deserializeKey("123.45", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeDouble() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals("Double", deserializer.getKeyClass().getName());
        assertEquals((Double) Double.parseDouble("123.45"), deserializer.deserializeKey("123.45", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeLocale() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals("Locale", deserializer.getKeyClass().getName());
        assertEquals(Locale.ENGLISH, deserializer.deserializeKey("en_US", mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeDate() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        assertEquals("Date", deserializer.getKeyClass().getName());
        Date date = new Date();
        assertEquals(TestUtils.MILLIS_PER_DAY * 3, deserializer.deserializeKey("3 days", mapper.getDeserializationContext()).getTime());
        assertEquals(date, deserializer.deserializeKey(TestUtils.formatISODate(date), mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeCalendar() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        assertEquals("Calendar", deserializer.getKeyClass().getName());
        Calendar calendar = Calendar.getInstance();
        assertEquals(TestUtils.MILLIS_PER_DAY, deserializer.deserializeKey("1 day", mapper.getDeserializationContext()).getTimeInMillis());
        assertEquals(calendar, deserializer.deserializeKey(TestUtils.formatISODate(calendar.getTime()), mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeUuid() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals("UUID", deserializer.getKeyClass().getName());
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, deserializer.deserializeKey(uuid.toString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeUri() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals("URI", deserializer.getKeyClass().getName());
        URI uri = new URI("http://www.example.com");
        assertEquals(uri, deserializer.deserializeKey(uri.toASCIIString(), mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeUrl() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals("URL", deserializer.getKeyClass().getName());
        URL url = new URL("http://www.example.com");
        assertEquals(url, deserializer.deserializeKey(url.toExternalForm(), mapper.getDeserializationContext()));
    }

    @Test
    public void testForTypeEnum() throws IOException {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        @JsonSubTypes({ @JsonSubTypes.Type(value = MyEnum.SubType1.class, name = "SUBTYPE1") })
        static class MyEnum {
            public static enum SubType1 { SUB_VALUE1, SUB_VALUE2 }
            public static enum SubType2 { SUB_VALUE3 }
        }

        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.SubType1.class);
        assertEquals("MyEnum$SubType1", deserializer.getKeyClass().getName());
        assertEquals(MyEnum.SubType1.SUB_VALUE1, deserializer.deserializeKey("SUBTYPE1_SUB_VALUE1", mapper.getDeserializationContext()));
        assertEquals(MyEnum.SubType1.SUB_VALUE2, deserializer.deserializeKey("SUBTYPE1_SUB_VALUE2", mapper.getDeserializationContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testForTypeUnknownEnum() throws IOException {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        static class MyEnum {
            public static enum SubType1 { SUB_VALUE1, SUB_VALUE2 }
            public static enum SubType2 { SUB_VALUE3 }
        }

        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.SubType3.class);
        assertNull(deserializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testForTypeClass() throws IOException {
        ObjectKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertNull(deserializer);
    }
}