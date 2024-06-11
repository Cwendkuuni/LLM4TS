package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for StdKeyDeserializer class.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Boolean.class, name = "Boolean"),
        @JsonSubTypes.Type(value = Byte.class, name = "Byte"),
        @JsonSubTypes.Type(value = Short.class, name = "Short"),
        @JsonSubTypes.Type(value = Character.class, name = "Char"),
        @JsonSubTypes.Type(value = Integer.class, name = "Int"),
        @JsonSubTypes.Type(value = Long.class, name = "Long"),
        @JsonSubTypes.Type(value = Float.class, name = "Float"),
        @JsonSubTypes.Type(value = Double.class, name = "Double"),
        @JsonSubTypes.Type(value = Locale.class, name = "Locale"),
        @JsonSubTypes.Type(value = Date.class, name = "Date"),
        @JsonSubTypes.Type(value = Calendar.class, name = "Calendar"),
        @JsonSubTypes.Type(value = UUID.class, name = "UUID"),
        @JsonSubTypes.Type(value = URI.class, name = "URI"),
        @JsonSubTypes.Type(value = URL.class, name = "URL"),
        @JsonSubTypes.Type(value = Class.class, name = "Class"),
        @JsonSubTypes.Type(value = Currency.class, name = "Currency")
})
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBoolean() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);

        Object key = deserializer.deserializeKey("true", objectMapper.getDeserializationContext());
        assertEquals(Boolean.TRUE, key);

        key = deserializer.deserializeKey("false", objectMapper.getDeserializationContext());
        assertEquals(Boolean.FALSE, key);
    }

    @Test
    public void testByte() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Object key = deserializer.deserializeKey("1", objectMapper.getDeserializationContext());
        assertEquals((Byte) 1, key);

        try {
            key = deserializer.deserializeKey("128", objectMapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testShort() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);

        Object key = deserializer.deserializeKey("1", objectMapper.getDeserializationContext());
        assertEquals((Short) 1, key);

        try {
            key = deserializer.deserializeKey("32768", objectMapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testChar() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);

        Object key = deserializer.deserializeKey("a", objectMapper.getDeserializationContext());
        assertEquals((Character) 'a', key);
    }

    @Test
    public void testInt() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);

        Object key = deserializer.deserializeKey("1", objectMapper.getDeserializationContext());
        assertEquals(1, key);
    }

    @Test
    public void testLong() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);

        Object key = deserializer.deserializeKey("1", objectMapper.getDeserializationContext());
        assertEquals(1L, key);

        Object keyLongMax = deserializer.deserializeKey(Long.MAX_VALUE + "", objectMapper.getDeserializationContext());
        assertEquals(Long.MAX_VALUE, keyLongMax);

        Object keyLongMin = deserializer.deserializeKey(Long.MIN_VALUE + "", objectMapper.getDeserializationContext());
        assertEquals(Long.MIN_VALUE, keyLongMin);
    }

    @Test
    public void testFloat() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);

        Object key = deserializer.deserializeKey("1.0", objectMapper.getDeserializationContext());
        assertEquals((Float) 1.0f, key);
    }

    @Test
    public void testDouble() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Object key = deserializer.deserializeKey("1.0", objectMapper.getDeserializationContext());
        assertEquals((Double) 1.0, key);
    }

    @Test
    public void testLocale() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Object key = deserializer.deserializeKey("en_US", objectMapper.getDeserializationContext());
        assertEquals(Locale.US, key);
    }

    @Test
    public void testDate() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);

        Object key = deserializer.deserializeKey("2022-03-01", objectMapper.getDeserializationContext());
        assertEquals(new Date(2022, 2, 1), key);
    }

    @Test
    public void testCalendar() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);

        Object key = deserializer.deserializeKey("2022-03-01", objectMapper.getDeserializationContext());
        assertEquals(Calendar.getInstance().set(2022, 2, 1), key);
    }

    @Test
    public void testUUID() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);

        Object key = deserializer.deserializeKey("00000000-0000-0000-0000-000000000000", objectMapper.getDeserializationContext());
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), key);
    }

    @Test
    public void testURI() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);

        Object key = deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        assertEquals(new URI("http://example.com"), key);
    }

    @Test
    public void testURL() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);

        Object key = deserializer.deserializeKey("http://example.com", objectMapper.getDeserializationContext());
        assertEquals(new URL("http://example.com"), key);
    }

    @Test
    public void testClass() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String[].class);

        Object key = deserializer.deserializeKey("[Ljava.lang.String;", objectMapper.getDeserializationContext());
        assertEquals(new String[0].getClass(), key.getClass());
    }

    @Test
    public void testCurrency() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Currency.class);

        Object key = deserializer.deserializeKey("USD", objectMapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), key);
    }

    @Test
    public void testUnknownKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        try {
            deserializer.deserializeKey("unknownType", objectMapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testNullKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        Object key = deserializer.deserializeKey(null, objectMapper.getDeserializationContext());
        assertNull(key);
    }

    @Test
    public void testUnsupportedType() throws IOException, JsonProcessingException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Object.class);

        try {
            deserializer.deserializeKey("Boolean", objectMapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testEnum() throws IOException, JsonProcessingException {
        enum MyEnum {
            ONE("One"),
            TWO("Two"),
            THREE("Three");

            private final String name;

            MyEnum(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();

        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);

        String json = objectWriter.writeValueAsString(EnumSet.allOf(MyEnum.class));

        Object key = deserializer.deserializeKey("Two", objectMapper.getDeserializationContext());
        assertEquals(MyEnum.TWO, key);
    }
}