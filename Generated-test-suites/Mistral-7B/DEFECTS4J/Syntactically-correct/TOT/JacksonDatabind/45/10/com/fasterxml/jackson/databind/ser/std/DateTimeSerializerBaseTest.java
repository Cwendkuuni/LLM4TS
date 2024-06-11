package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Test;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

    @Test
    public void testDateTimeSerializerBaseWithDefaultSettings() throws Exception {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        JsonNode jsonNode = mapper.writeValueAsTree(new Date());
        assertEquals("\"2022-12-11T12:13:14.567Z\"", jsonNode.toString());

        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        assertEquals(deserializedDate.getTime(), new Date().getTime());
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws Exception {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer()
                .withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        JsonNode jsonNode = mapper.writeValueAsTree(new Date());
        assertEquals("\"11-12-2022 12:13:14\"", jsonNode.toString());

        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        assertEquals(deserializedDate.getTime(), new Date().getTime());
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestamp() throws Exception {
        DateTimeSerializerBase<Long> longDateTimeSerializer = new StdLongSerializer();
        JsonNode jsonNode = mapper.writeValueAsTree(Instant.now().getEpochSecond());
        assertTrue(jsonNode.isNumber());

        long deserializedTimestamp = mapper.readValue(jsonNode.toString(), Long.class);
        assertEquals(deserializedTimestamp, Instant.now().getEpochSecond());
    }

    @Test
    public void testDateTimeSerializerBaseWithJsonFormatAnnotation() throws Exception {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        Date date = new Date();
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        JsonNode jsonNode = mapper.writeValueAsTree(date);
        assertEquals("\"11-12-2022 12:13:14\"", jsonNode.toString());

        Date deserializedDate = mapper.readValue(jsonNode.toString(), Date.class);
        assertEquals(deserializedDate.getTime(), new Date().getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseWithNullSerializerProvider() throws Exception {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        dateTimeSerializer.createContextual(null, null);
    }
}