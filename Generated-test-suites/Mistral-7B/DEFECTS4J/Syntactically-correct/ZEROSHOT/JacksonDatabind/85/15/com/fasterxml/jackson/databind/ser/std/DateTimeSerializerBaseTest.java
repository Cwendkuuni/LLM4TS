package com.example.junit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Before
    public void setup() {
        // Initialize mocks
        ObjectNode jsonNode = mock(ObjectNode.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCodec()).thenReturn(new ObjectMapper());
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        when(objectMapper.getSerializerProvider()).thenReturn(serializers);
        when(jsonParser.getCodec()).thenReturn(objectMapper);
        when(jsonParser.getProvider()).thenReturn(serializers);
        when(jsonGenerator.getCodec()).thenReturn(objectMapper);
        when(jsonGenerator.getProvider()).thenReturn(serializers);
    }

    @org.junit.Test
    public void testCreateContextual_Default() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        assertEquals(serializer.getClass(), serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testCreateContextual_UseTimestamp() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null);
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testCreateContextual_CustomFormat_String() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, DATE_FORMAT);
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testCreateContextual_CustomFormat_DateTime() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, DATETIME_FORMAT);
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testCreateContextual_CustomFormat_WithLocale() throws IOException {
        DateTimeSerializerBase<Locale> serializer = new DateTimeSerializerBase<Locale>(Locale.class, false, DATE_FORMAT);
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testCreateContextual_CustomFormat_WithTimeZone() throws IOException {
        DateTimeSerializerBase<TimeZone> serializer = new DateTimeSerializerBase<TimeZone>(TimeZone.class, false, DATE_FORMAT);
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testCreateContextual_CustomFormat_WithLocaleAndTimeZone() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, DATETIME_FORMAT);
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.createContextual(mock(SerializerProvider.class), null).getClass());
    }

    @org.junit.Test
    public void testSerialize_UseTimestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        Long value = System.currentTimeMillis();

        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(value, jsonGenerator, serializers);

        ObjectNode rootNode = mock(ObjectNode.class);
        when(jsonGenerator.getObject().get("@type")).thenReturn(rootNode);

        assertEquals("number", rootNode.get("@type").asText());
        assertEquals(value.toString(), jsonGenerator.getOutputValueAsString());
    }

    @org.junit.Test
    public void testSerialize_Default() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        Long value = System.currentTimeMillis();

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(value, jsonGenerator, serializers);

        ObjectNode rootNode = mock(ObjectNode.class);
        when(jsonGenerator.getObject()).thenReturn(rootNode);

        assertEquals("string", rootNode.get("@type").asText());
        assertEquals(DATE_FORMAT.format(new java.util.Date(value)), jsonGenerator.getOutputValueAsString());
    }

    @org.junit.Test
    public void testSerialize_CustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, DATE_FORMAT);
        Date value = new Date();

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(value, jsonGenerator, serializers);

        ObjectNode rootNode = mock(ObjectNode.class);
        when(jsonGenerator.getObject()).thenReturn(rootNode);

        assertEquals("string", rootNode.get("@type").asText());
        assertEquals(DATE_FORMAT.format(value), jsonGenerator.getOutputValueAsString());
    }

    @org.junit.Test
    public void testSerialize_CustomFormat_WithLocale() throws IOException {
        DateTimeSerializerBase<Locale> serializer = new DateTimeSerializerBase<Locale>(Locale.class, false, DATE_FORMAT);
        Locale value = Locale.US;

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(value, jsonGenerator, serializers);

        ObjectNode rootNode = mock(ObjectNode.class);
        when(jsonGenerator.getObject()).thenReturn(rootNode);

        assertEquals("string", rootNode.get("@type").asText());
        assertEquals(DATE_FORMAT.format(new java.util.Date(System.currentTimeMillis())), jsonGenerator.getOutputValueAsString());
        assertEquals(value.toString(), rootNode.get("value").asText());
    }

    @org.junit.Test
    public void testSerialize_CustomFormat_WithTimeZone() throws IOException {
        DateTimeSerializerBase<TimeZone> serializer = new DateTimeSerializerBase<TimeZone>(TimeZone.class, false, DATETIME_FORMAT);
        TimeZone value = TimeZone.getTimeZone("UTC");

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(value, jsonGenerator, serializers);

        ObjectNode rootNode = mock(ObjectNode.class);
        when(jsonGenerator.getObject()).thenReturn(rootNode);

        assertEquals("string", rootNode.get("@type").asText());
        assertEquals(DATETIME_FORMAT.format(new java.util.Date(System.currentTimeMillis())), jsonGenerator.getOutputValueAsString());
        assertEquals(value.getID(), rootNode.get("value").asText());
    }

    @org.junit.Test
    public void testSerialize_CustomFormat_WithLocaleAndTimeZone() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, DATETIME_FORMAT);
        Date value = new Date();

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(value, jsonGenerator, serializers);

        ObjectNode rootNode = mock(ObjectNode.class);
        when(jsonGenerator.getObject()).thenReturn(rootNode);

        assertEquals("string", rootNode.get("@type").asText());
        assertEquals(DATETIME_FORMAT.format(value), jsonGenerator.getOutputValueAsString());
    }

    @org.junit.Test
    public void testGetSchema() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);

        assertEquals("number", serializer.getSchema(mock(SerializerProvider.class), null).get("type").asText());
    }

    @org.junit.Test
    public void testGetSchema_Default() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        assertEquals("string", serializer.getSchema(mock(SerializerProvider.class), null).get("type").asText());
    }

    @org.junit.Test
    public void testGetSchema_CustomFormat() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, DATE_FORMAT);

        assertEquals("string", serializer.getSchema(mock(SerializerProvider.class), null).get("type").asText());
    }
}