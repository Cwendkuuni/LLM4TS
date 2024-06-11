package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDateWithDefaultSettings() throws IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);
        Date date = new Date();

        JsonNode result = dateSerializer.serializeWithType(date, null, objectMapper.getSerializationConfig(), JsonSerializer.NullValueHandler.EMPTY_STRING);

        ObjectNode jsonNode = (ObjectNode) result;
        assertNull(jsonNode.get("_class"));
        assertEquals("\"2022-12-31T00:00:00.000Z\"", jsonNode.getTextValue());
    }

    @Test
    public void testSerializeDateWithCustomFormat() throws IOException, ParseException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);
        Date date = new Date();

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(timeZone);

        JsonNode result = dateSerializer.createContextual(objectMapper.getSerializationConfig(), null)
                .withFormat(false, customFormat)
                .serializeValue(date, null, objectMapper.getSerializationConfig());

        ObjectNode jsonNode = (ObjectNode) result;
        assertEquals("\"2022-12-31 00:00:00 UTC\"", jsonNode.getTextValue());
    }

    @Test
    public void testSerializeDateAsTimestamp() throws IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);
        Date date = new Date();

        JsonNode result = dateSerializer.withFormat(true, null).serializeValue(date, null, objectMapper.getSerializationConfig());

        ObjectNode jsonNode = (ObjectNode) result;
        assertEquals("2022-12-31T00:00:00.000Z", jsonNode.getTextValue());
    }

    @Test
    public void testSerializeNullDate() throws IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);
        Date nullDate = null;

        JsonNode result = dateSerializer.serializeWithType(nullDate, null, objectMapper.getSerializationConfig(), JsonSerializer.NullValueHandler.EMPTY_STRING);

        ObjectNode jsonNode = (ObjectNode) result;
        assertNull(jsonNode.get("_class"));
        assertNull(jsonNode.getTextValue());
    }

    @Test
    public void testDeserializeDateWithDefaultSettings() throws IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);

        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().readTree("{\"date\":\"2022-12-31T00:00:00.000Z\"}");

        Date result = dateSerializer.deserialize(jsonNode, java.util.Date.class, null);

        assertEquals(new Date(), result);
    }

    @Test
    public void testDeserializeDateWithCustomFormat() throws IOException, ParseException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);

        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        customFormat.setTimeZone(timeZone);

        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().readTree("{\"date\":\"2022-12-31 00:00:00 UTC\"}");

        Date result = dateSerializer.createContextual(objectMapper.getSerializationConfig(), null)
                .withFormat(false, customFormat)
                .deserialize(jsonNode, java.util.Date.class, null);

        assertEquals(new Date(), result);
    }

    @Test
    public void testDeserializeDateAsTimestamp() throws IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);

        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().readTree("{\"date\":\"2022-12-31T00:00:00.000Z\"}");

        Date result = dateSerializer.withFormat(true, null).deserialize(jsonNode, java.util.Date.class, null);

        assertEquals(new Date(1671542400000L), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullValue() throws IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = objectMapper.findValueSerializer(java.util.Date.class);

        ObjectNode jsonNode = ObjectMapper.getDefaultInstance().readTree("{\"date\":null}");

        dateSerializer.deserialize(jsonNode, java.util.Date.class, null);
    }
}