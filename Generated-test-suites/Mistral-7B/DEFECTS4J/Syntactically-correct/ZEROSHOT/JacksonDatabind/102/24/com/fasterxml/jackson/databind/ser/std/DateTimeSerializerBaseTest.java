package com.example.junit;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<Date> dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }

            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                super._serializeAsString(value, gen, serializers);
            }
        };

        ObjectMapper mapper = new ObjectMapper();

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
        @JsonSerialize(using = DateTimeSerializerBase.class)
        static class TestDate {
            private Date date;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }

        TestDate testDate = new TestDate();
        testDate.setDate(new Date());

        String json = mapper.writeValueAsString(testDate);
        assertEquals("\"2023-03-23 14:35:12 UTC\"", json);

        TestDate deserializedTestDate = mapper.readValue(json, TestDate.class);
        assertEquals(testDate.getDate(), deserializedTestDate.getDate());
    }
}