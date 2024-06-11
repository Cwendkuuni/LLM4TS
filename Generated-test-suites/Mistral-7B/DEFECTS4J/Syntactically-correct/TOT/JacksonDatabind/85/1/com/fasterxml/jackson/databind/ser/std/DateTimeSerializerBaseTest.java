package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = new StdLongSerializer(Long.TYPE, false, null);
        ObjectMapper mapper = new ObjectMapper();
        Long dateValue = 1494824000000L; // 2017-01-01 00:00:00 (UTC) as milliseconds
        JsonNode result = mapper.writeValueAsTree(dateValue, new DateTimeSerializerBase.JsonSerializerParams());
        assertEquals("2017-01-01T00:00:00Z", result.asText());

        // Test serialization as numeric timestamp
        DateTimeSerializerBase<Long> numericSerializer = serializer.withFormat(true, null);
        JsonNode numericResult = mapper.writeValueAsTree(dateValue, numericSerializer);
        assertEquals("2017-01-01T00:00:00.000Z", numericResult.asText());
    }

    @Test
    public void testCustomFormatSerialization() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<String> serializer = new StdStringSerializer(String.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<String> customSerializer = serializer.withFormat(false, customFormat);
        String dateString = "25-06-2017 14:30:00 UTC";
        JsonNode result = mapper.readTree(mapper.writeValueAsString(dateString, customSerializer));
        assertEquals("2017-06-25T14:30:00Z", result.asText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = new StdLongSerializer(Long.TYPE, false, null);
        serializer.createContextual(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedJsonFormat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Integer> serializer = new StdIntSerializer(Integer.TYPE, false, null);
        try {
            JsonNode result = mapper.writeValueAsTree(new GregorianCalendar(2017, 6, 25).getTimeInMillis(), serializer);
            fail("Expected JsonMappingException but got: " + result);
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testEmptySerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Object> serializer = new StdObjectSerializer(Object.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.writeValueAsTree(null, serializer);
        assertEquals(NullNode.instance, result);
    }

    @Test
    public void testEmptySerializationWithProvider() throws JsonProcessingException {
        DateTimeSerializerBase<Object> serializer = new StdObjectSerializer(Object.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = mapper.writeValueAsTree(null, serializer, mapper.getSerializerProvider());
        assertEquals(NullNode.instance, result);
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = new StdLongSerializer(Long.TYPE, false, null);
        JsonNode schema = serializer.getSchema(new ObjectMapper(), Long.TYPE);
        assertEquals("number", schema.asText());
    }
}