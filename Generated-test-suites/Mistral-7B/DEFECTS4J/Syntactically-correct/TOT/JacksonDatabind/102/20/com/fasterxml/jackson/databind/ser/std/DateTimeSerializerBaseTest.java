package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String serializeToString = mapper.writerWithView(JsonMapping.SerializerView.ALLOW_EMPTY_STRING.class)
                .writeValueAsString(date);

        JsonNode jsonNode = mapper.readTree(serializeToString);
        JsonNode expectedJsonNode = mapper.valueToTree(date);
        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String serializeToString = mapper.writerWithView(JsonMapping.SerializerView.ALLOW_EMPTY_STRING.class)
                .writeValueAsString(date);

        JsonNode jsonNode = mapper.readTree(serializeToString);
        JsonNode expectedJsonNode = mapper.valueToTree("25-06-2023");
        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testSerializationWithCustomFormatAndLocale() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH));
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String serializeToString = mapper.writerWithView(JsonMapping.SerializerView.ALLOW_EMPTY_STRING.class)
                .writeValueAsString(date);

        JsonNode jsonNode = mapper.readTree(serializeToString);
        JsonNode expectedJsonNode = mapper.valueToTree("25-06-2023");
        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
        expectedJsonNode = mapper.valueToTree(expectedFormat.format(date));
        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testSerializationWithCustomFormatAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss zzz"), TimeZone.getTimeZone("UTC"));
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String serializeToString = mapper.writerWithView(JsonMapping.SerializerView.ALLOW_EMPTY_STRING.class)
                .writeValueAsString(date);

        JsonNode jsonNode = mapper.readTree(serializeToString);
        JsonNode expectedJsonNode = mapper.valueToTree("25-06-2023 12:00:00 UTC");
        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss zzz", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        expectedJsonNode = mapper.valueToTree(expectedFormat.format(date));
        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testSerializationWithTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String serializeToString = mapper.writerWithView(JsonMapping.SerializerView.ALLOW_EMPTY_STRING.class)
                .writeValueAsString(date);

        JsonNode jsonNode = mapper.readTree(serializeToString);
        JsonNode expectedJsonNode = mapper.valueToTree(date.getTime());
        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testDeserialization() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"25-06-2023\"";
        Date deserializedDate = mapper.readerFor
                (Date.class).readValue(JsonFactory.instance.parse(jsonString));

        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expectedDate = expectedFormat.parse("25-06-2023");
        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"25-06-2023\"";
        Date deserializedDate = mapper.readerFor
                (Date.class).readValue(JsonFactory.instance.parse(jsonString));

        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date expectedDate = expectedFormat.parse("25-06-2023");
        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testDeserializationWithCustomFormatAndLocale() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"25-06-2023\"";
        Date deserializedDate = mapper.readerFor
                (Date.class).readValue(JsonFactory.instance.parse(jsonString));

        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
        Date expectedDate = expectedFormat.parse("25-06-2023");
        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testDeserializationWithCustomFormatAndTimeZone() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss zzz"), TimeZone.getTimeZone("UTC"));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"25-06-2023 12:00:00 UTC\"";
        Date deserializedDate = mapper.readerFor
                (Date.class).readValue(JsonFactory.instance.parse(jsonString));

        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss zzz", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date expectedDate = expectedFormat.parse("25-06-2023 12:00:00 UTC");
        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testDeserializationWithTimestamp() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "1655737200000";
        Date deserializedDate = mapper.readerFor
                (Date.class).readValue(JsonFactory.instance.parse(jsonString));

        Date expectedDate = new Date(1655737200000L);
        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testNullValueSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        Date date = null;
        String serializeToString = mapper.writerWithView(JsonMapping.SerializerView.ALLOW_EMPTY_STRING.class)
                .writeValueAsString(date);

        JsonNode jsonNode = mapper.readTree(serializeToString);
        assertNull(jsonNode);
    }

    @Test
    public void testNullValueDeserialization() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = JsonFactory.instance.parse("null");
        Date deserializedDate = mapper.readerFor(Date.class).readValue(jsonNode);
        assertNull(deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss"));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "\"25-06-2023-12:00:00\"";
        Date deserializedDate = mapper.readerFor(Date.class).readValue(JsonFactory.instance.parse(jsonString));
    }
}