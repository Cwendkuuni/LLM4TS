package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase_useTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        Date date = new Date();
        String serializedDate = dateTimeSerializer.serialize(date, new JsonGenerator(serializers.getOutputFactory()), serializers);
        assertNotNull(serializedDate);
        assertEquals("\"\\\"\\/Date(1638175431813)\"", serializedDate);

        Date deserializedDate = dateTimeSerializer.deserialize(null, JsonParser.parseString("\"\\\"\\/Date(1638175431813)\"\"", null), serializers, null);
        assertInstanceof(Date.class, deserializedDate);
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDateTimeSerializerBase_customFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US));
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        Date date = new Date();
        String serializedDate = dateTimeSerializer.serialize(date, new JsonGenerator(serializers.getOutputFactory()), serializers);
        String expected = "19-06-2023 14:34:55"; // assuming current date and time is 19-06-2023 14:34:55
        assertEquals(expected, serializedDate);

        Date deserializedDate = dateTimeSerializer.deserialize(null, JsonParser.parseString(expected, null), serializers, null);
        assertInstanceof(Date.class, deserializedDate);
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDateTimeSerializerBase_nullValue() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        assertNull(dateTimeSerializer.serialize(null, new JsonGenerator(serializers.getOutputFactory()), serializers));
    }

    @Test
    public void testDateTimeSerializerBase_emptyValue() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        // As per the comments in the `isEmpty()` method, the class does not consider a timestamp of 0 as an empty value
        Date emptyValue = new Date(0L);
        String serializedEmptyValue = dateTimeSerializer.serialize(emptyValue, new JsonGenerator(serializers.getOutputFactory()), serializers);
        assertNotNull(serializedEmptyValue);
        assertFalse(isEmptyOrNull(serializedEmptyValue));
    }

    @Test
    public void testDateTimeSerializerBase_withFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = dateTimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US));

        Date date = new Date();
        String serializedDate = dateTimeSerializerWithFormat.serialize(date, new JsonGenerator(new ObjectMapper().getSerializerProvider().getOutputFactory()), new ObjectMapper().getSerializerProvider());
        String expected = "19-06-2023 14:34:55"; // assuming current date and time is 19-06-2023 14:34:55
        assertEquals(expected, serializedDate);
    }

    @Test
    public void testDateTimeSerializerBase_withFormat_useTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
        DateTimeSerializerBase<Date> dateTimeSerializerWithFormat = dateTimeSerializer.withFormat(true, null);

        Date date = new Date();
        String serializedDate = dateTimeSerializerWithFormat.serialize(date, new JsonGenerator(new ObjectMapper().getSerializerProvider().getOutputFactory()), new ObjectMapper().getSerializerProvider());
        assertNotNull(serializedDate);
        assertEquals("\"\\\"\\/Date(1638175431813)\"", serializedDate);
    }

    private boolean isEmptyOrNull(String value) {
        return (value == null || value.isEmpty());
    }
}