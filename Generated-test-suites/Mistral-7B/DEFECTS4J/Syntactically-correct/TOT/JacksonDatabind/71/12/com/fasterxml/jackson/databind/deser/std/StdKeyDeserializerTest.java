package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class StdKeyDeserializerTest {

    private JsonMapper mapper;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        mapper = new JsonMapper();
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        mapper = null;
        objectMapper = null;
    }

    @Test
    public void testBooleanType() throws IOException {
        String json = "{\"key\":true}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", mapper.getDeserializationContext()));
    }

    @Test
    public void testByteType() throws IOException {
        String json = "{\"key\":123}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        assertEquals((Byte) new Byte((byte) 123), deserializer.deserializeKey("123", mapper.getDeserializationContext()));
    }

    @Test
    public void testShortType() throws IOException {
        String json = "{\"key\":12345}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        assertEquals((Short) Short.parseShort("12345"), deserializer.deserializeKey("12345", mapper.getDeserializationContext()));
    }

    @Test
    public void testCharType() throws IOException {
        String json = "{\"key\":'a'}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        assertEquals(Character.valueOf('a'), deserializer.deserializeKey("a", mapper.getDeserializationContext()));
    }

    @Test
    public void testIntType() throws IOException {
        String json = "{\"key\":123456}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        assertEquals(123456, deserializer.deserializeKey("123456", mapper.getDeserializationContext()));
    }

    @Test
    public void testLongType() throws IOException {
        String json = "{\"key\":123456789012345}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        assertEquals(123456789012345L, deserializer.deserializeKey("123456789012345", mapper.getDeserializationContext()));
    }

    @Test
    public void testFloatType() throws IOException {
        String json = "{\"key\":123.45f}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        assertEquals(Float.parseFloat("123.45"), deserializer.deserializeKey("123.45", mapper.getDeserializationContext()));
    }

    @Test
    public void testDoubleType() throws IOException {
        String json = "{\"key\":123.45}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        assertEquals(123.45, deserializer.deserializeKey("123.45", mapper.getDeserializationContext()));
    }

    @Test
    public void testLocaleType() throws IOException {
        String json = "{\"key\":\"en_US\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        assertEquals(Locale.US, deserializer.deserializeKey("en_US", mapper.getDeserializationContext()));
    }

    @Test
    public void testDateType() throws IOException {
        String json = "{\"key\":\"2022-01-01\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        assertEquals(new Date(0), deserializer.deserializeKey("2022-01-01", mapper.getDeserializationContext()));
    }

    @Test
    public void testCalendarType() throws IOException {
        String json = "{\"key\":\"2022-01-01T12:00:00Z\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        GregorianCalendar expected = new GregorianCalendar(2022, 0, 1, 12, 0, 0);
        assertEquals(expected, deserializer.deserializeKey("2022-01-01T12:00:00Z", mapper.getDeserializationContext()));
    }

    @Test
    public void testUUIDType() throws IOException {
        String json = "{\"key\":\"123e4567-e89b-12d3-a456-426614174000\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        assertEquals(new UUID("123e4567-e89b-12d3-a456-426614174000"), deserializer.deserializeKey("123e4567-e89b-12d3-a456-426614174000", mapper.getDeserializationContext()));
    }

    @Test
    public void testURIType() throws IOException {
        String json = "{\"key\":\"https://www.example.com\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        assertEquals(new URI("https://www.example.com"), deserializer.deserializeKey("https://www.example.com", mapper.getDeserializationContext()));
    }

    @Test
    public void testURLType() throws IOException {
        String json = "{\"key\":\"https://www.example.com\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        assertEquals(new URL("https://www.example.com"), deserializer.deserializeKey("https://www.example.com", mapper.getDeserializationContext()));
    }

    @Test
    public void testEnumType() throws IOException {
        String json = "{\"key\":\"EAST\"}";
        ObjectNode node = objectMapper.readTree(json);
        EnumSet<Direction> expected = EnumSet.of(Direction.EAST);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Direction.class);
        assertEquals(expected, (EnumSet<Direction>) deserializer.deserializeKey("EAST", mapper.getDeserializationContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownType() throws IOException {
        String json = "{\"key\":123}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.TYPE);
        deserializer.deserializeKey("123", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidBooleanType() throws IOException {
        String json = "{\"key\":\"invalid\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        deserializer.deserializeKey("invalid", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidByteType() throws IOException {
        String json = "{\"key\":\"invalidByteValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);
        deserializer.deserializeKey("invalidByteValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidShortType() throws IOException {
        String json = "{\"key\":\"invalidShortValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Short.class);
        deserializer.deserializeKey("invalidShortValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCharType() throws IOException {
        String json = "{\"key\":\"invalidCharValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Character.class);
        deserializer.deserializeKey("invalidCharValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidIntType() throws IOException {
        String json = "{\"key\":\"invalidIntValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
        deserializer.deserializeKey("invalidIntValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidLongType() throws IOException {
        String json = "{\"key\":\"invalidLongValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
        deserializer.deserializeKey("invalidLongValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFloatType() throws IOException {
        String json = "{\"key\":\"invalidFloatValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Float.class);
        deserializer.deserializeKey("invalidFloatValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDoubleType() throws IOException {
        String json = "{\"key\":\"invalidDoubleValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);
        deserializer.deserializeKey("invalidDoubleValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidLocaleType() throws IOException {
        String json = "{\"key\":\"invalidLocaleValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);
        deserializer.deserializeKey("invalidLocaleValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDateType() throws IOException {
        String json = "{\"key\":\"invalidDateValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
        deserializer.deserializeKey("invalidDateValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCalendarType() throws IOException {
        String json = "{\"key\":\"invalidCalendarValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Calendar.class);
        deserializer.deserializeKey("invalidCalendarValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidUUIDType() throws IOException {
        String json = "{\"key\":\"invalidUUIDValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(UUID.class);
        deserializer.deserializeKey("invalidUUIDValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidURIType() throws IOException {
        String json = "{\"key\":\"invalidURIValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URI.class);
        deserializer.deserializeKey("invalidURIValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidURLType() throws IOException {
        String json = "{\"key\":\"invalidURLValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(URL.class);
        deserializer.deserializeKey("invalidURLValue", mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidEnumType() throws IOException {
        String json = "{\"key\":\"invalidEnumValue\"}";
        ObjectNode node = objectMapper.readTree(json);
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Direction.class);
        deserializer.deserializeKey("invalidEnumValue", mapper.getDeserializationContext());
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST
    }
}