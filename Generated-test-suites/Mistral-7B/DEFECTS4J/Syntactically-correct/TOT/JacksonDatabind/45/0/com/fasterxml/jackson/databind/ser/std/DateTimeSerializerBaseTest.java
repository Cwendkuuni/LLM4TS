package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testDefaultSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        Date date = new Date();
        String json = mapper.writerWithRootValueSerializer(dateSerializer).writeValueAsString(date);

        assertEquals("\"2023-03-01T00:00:00.000Z\"", json);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));

        Date date = new Date();
        String json = mapper.writerWithRootValueSerializer(dateSerializer).writeValueAsString(date);

        assertEquals("\"2023-03-01 12:00:00 +0100\"", json);
    }

    @Test
    public void testSerializationWithUseTimestamp() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(true, null);

        Date date = new Date();
        String json = mapper.writerWithRootValueSerializer(dateSerializer).writeValueAsString(date);

        assertEquals("2023-03-01T00:00:00.000Z", json);

        JsonParser parser = mapper.getFactory().createParser(new TextNode("2023-03-01T00:00:00.000Z").asText());
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        String json = mapper.writerWithRootValueSerializer(dateSerializer).writeValueAsString(null);
        assertNull(json);

        JsonParser parser = mapper.getFactory().createParser(new TextNode("null").asText());
        Date deserializedDate = mapper.readValue(parser, Date.class);
        assertNull(deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<String> stringSerializer = new StdStringSerializer();

        String json = mapper.writerWithRootValueSerializer(stringSerializer).writeValueAsString(new Date());

        // This should fail because the StdStringSerializer is not designed to handle dates
    }

    @Test(expected = IOException.class)
    public void testSerializationWithCustomFormatAndInvalidFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(false, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-zz"));

        Date date = new Date();

        try {
            mapper.writerWithRootValueSerializer(dateSerializer).writeValueAsString(date);
            // This should fail because the custom format is invalid
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializationWithDefaultFormat() throws ParseException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        String json = "\"2023-03-01T00:00:00.000Z\"";
        Date date = mapper.readerForUpdating(Date.class).readValue(json);

        assertEquals(new Date(1678144000000L), date);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws ParseException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));

        String json = "\"2023-03-01 12:00:00 +0100\"";
        Date date = mapper.readerForUpdating(Date.class).readValue(json);

        assertEquals(new Date(1678147600000L), date);
    }

    @Test
    public void testDeserializationWithUseTimestamp() throws ParseException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(true, null);

        String json = "2023-03-01T00:00:00.000Z";
        Date date = mapper.readerForUpdating(Date.class).readValue(new TextNode(json).asText());

        assertEquals(new Date(1678144000000L), date);
    }

    @Test
    public void testDeserializationWithNullValue() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        String json = "null";
        Date date = mapper.readerForUpdating(Date.class).readValue(new TextNode(json).asText());

        assertNull(date);
    }

    @Test(expected = IOException.class)
    public void testDeserializationWithInvalidFormat() throws ParseException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        String json = "\"invalid_format\"";

        try {
            mapper.readerForUpdating(Date.class).readValue(new TextNode(json).asText());
            // This should fail because the JSON string is not a valid date format
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testDeserializationWithCustomFormatAndInvalidFormat() throws ParseException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(false, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-zz"));

        String json = "\"2023-03-01 12:00:00 +0100\"";

        try {
            mapper.readerForUpdating(Date.class).readValue(new TextNode(json).asText());
            // This should fail because the custom format is invalid
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}