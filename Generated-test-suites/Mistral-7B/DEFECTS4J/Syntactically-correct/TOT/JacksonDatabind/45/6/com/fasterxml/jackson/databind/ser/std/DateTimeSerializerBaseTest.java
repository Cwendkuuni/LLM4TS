package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDateTimeSerializerBase_useTimestamp_defaultFormat() throws JsonProcessingException, JsonGenerationException {
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        JsonNode result = mapper.writeValueAsTree(1L, longSerializer);
        assertEquals("1970-01-01T00:00:01.000Z", result.toString());
    }

    @Test
    public void testDateTimeSerializerBase_customFormat_stringFormat() throws JsonProcessingException, JsonGenerationException {
        DateTimeSerializerBase<JavaDate> dateSerializer = new DateTimeSerializerBase<JavaDate>(JavaDate.class, false, new SimpleDateFormat("dd-MM-yyyy"));
        JavaDate date = new JavaDate();
        date.setTime(1633043200000L); // 03-Apr-2022
        JsonNode result = mapper.writeValueAsTree(date, dateSerializer);
        assertEquals("\"03-Apr-2022\"", result.toString());
    }

    @Test
    public void testDateTimeSerializerBase_customFormat_invalidFormat() throws JsonProcessingException, JsonGenerationException {
        DateTimeSerializerBase<JavaDate> dateSerializer = new DateTimeSerializerBase<JavaDate>(JavaDate.class, false, new SimpleDateFormat("dd-MM-yyyy-dd"));
        JavaDate date = new JavaDate();
        date.setTime(1633043200000L); // 03-Apr-2022
        try {
            mapper.writeValueAsTree(date, dateSerializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Invalid format: \"dd-MM-yyyy-dd\"", e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBase_customFormat_localeAndTimeZone() throws JsonProcessingException, JsonGenerationException {
        DateTimeSerializerBase<JavaDate> dateSerializer = new DateTimeSerializerBase<JavaDate>(JavaDate.class, false, new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH, TimeZone.getTimeZone("Europe/Paris")));
        JavaDate date = new JavaDate();
        date.setTime(1633043200000L); // 03-Apr-2022
        JsonNode result = mapper.writeValueAsTree(date, dateSerializer);
        assertEquals("\"03-Avr-2022\"", result.toString());
    }

    @Test
    public void testDateTimeSerializerBase_emptyValue() throws JsonProcessingException {
        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonNode result = mapper.writeValueAsTree(null, stringSerializer);
        assertEquals("null", result.toString());
    }

    @Test
    public void testDateTimeSerializerBase_emptyValueWithTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        JsonNode result = mapper.writeValueAsTree(null, longSerializer);
        assertEquals("0", result.toString());
    }

    @Test
    public void testDateTimeSerializerBase_customFormatWithTimestamp() throws JsonProcessingException, JsonGenerationException {
        DateTimeSerializerBase<JavaDate> dateSerializer = new DateTimeSerializerBase<JavaDate>(JavaDate.class, true, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        JavaDate date = new JavaDate();
        date.setTime(1633043200000L); // 03-Apr-2022
        JsonNode result = mapper.writeValueAsTree(date, dateSerializer);
        assertEquals("1633043200000", result.toString());
    }

    @Test
    public void testDateTimeSerializerBase_customFormatWithInvalidAnnotation() {
        class MyDate {
            @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+1")
            private JavaDate date;
        }

        DateTimeSerializerBase<MyDate> dateSerializer = new DateTimeSerializerBase<MyDate>(MyDate.class, false, null);
        MyDate myDate = new MyDate();
        try {
            mapper.writeValueAsTree(myDate, dateSerializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not compatible with @JsonFormat(..., timezone=GMT+1), " +
                    "use DateTimeSerializerBase(MyDate.class, false, SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss'Z'\", Locale.getDefault(), TimeZone.getDefault())) instead", e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBase_customFormatWithTimestampAndInvalidAnnotation() {
        class MyDate {
            @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+1")
            private Long date;
        }

        DateTimeSerializerBase<MyDate> dateSerializer = new DateTimeSerializerBase<MyDate>(MyDate.class, true, null);
        MyDate myDate = new MyDate();
        try {
            mapper.writeValueAsTree(myDate, dateSerializer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not compatible with @JsonFormat(..., timezone=GMT+1), " +
                    "use DateTimeSerializerBase(MyDate.class, true, SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss'Z'\", Locale.getDefault(), TimeZone.getDefault())) instead", e.getMessage());
        }
    }
}