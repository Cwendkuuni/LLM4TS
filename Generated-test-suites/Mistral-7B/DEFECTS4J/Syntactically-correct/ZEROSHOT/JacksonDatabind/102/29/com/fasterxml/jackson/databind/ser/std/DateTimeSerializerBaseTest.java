package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;
    private SerializerProvider serializers;
    private JsonGenerator generator;

    @Before
    public void setup() {
        mapper = new JsonMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        serializers = mapper.getSerializerProvider();
        generator = mock(JsonGenerator.class);
    }

    @Test
    public void testNull() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        assertNull(serializer.serialize(null, generator, serializers));
    }

    @Test
    public void testEmptyDate() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        assertNull(serializer.serialize(new Date(0), generator, serializers));
    }

    @Test
    public void testTimestamp() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, true, null);
        serializer.serialize(new Date(0), generator, serializers);
        assertEquals(0L, generator.getOutputValue().asLong());
    }

    @Test
    public void testCustomFormat() throws IOException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        Date date = new Date();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonNode schema = serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Date.class, mapper.getTypeFactory()));
        assertEquals("string", schema.getNodeType());
        assertEquals("date-time", schema.textValue());

        serializer.serialize(date, generator, serializers);
        assertEquals(customFormat.format(date), generator.getOutputValue().asText());
    }

    @Test
    public void testCustomFormatWithLocale() throws IOException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
        customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        Date date = new Date();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonNode schema = serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Date.class, mapper.getTypeFactory()));
        assertEquals("string", schema.getNodeType());
        assertEquals("date-time", schema.textValue());

        serializer.serialize(date, generator, serializers);
        assertEquals(customFormat.format(date), generator.getOutputValue().asText());
    }

    @Test
    public void testCustomFormatWithTimeZone() throws IOException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        Date date = new Date();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonNode schema = serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Date.class, mapper.getTypeFactory()));
        assertEquals("string", schema.getNodeType());
        assertEquals("date-time", schema.textValue());

        serializer.serialize(date, generator, serializers);
        assertEquals(customFormat.format(date), generator.getOutputValue().asText());
    }

    @Test
    public void testCustomFormatWithLocaleAndTimeZone() throws IOException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.FRENCH);
        customFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);
        Date date = new Date();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonNode schema = serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Date.class, mapper.getTypeFactory()));
        assertEquals("string", schema.getNodeType());
        assertEquals("date-time", schema.textValue());

        serializer.serialize(date, generator, serializers);
        assertEquals(customFormat.format(date), generator.getOutputValue().asText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() throws IOException {
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, null);
        serializer.serialize(new Date(), generator, null);
    }

    @Test
    public void testThreadSafeReusableCustomFormat() throws IOException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> serializer = createSerializer(Date.class, false, customFormat);

        Date date1 = new Date();
        Date date2 = new Date();

        serializer.serialize(date1, generator, serializers);
        assertEquals(customFormat.format(date1), generator.getOutputValue().asText());

        serializer.serialize(date2, generator, serializers);
        assertEquals(customFormat.format(date2), generator.getOutputValue().asText());

        // Test that `_reusedCustomFormat` is thread-safe
        DateTimeSerializerBase<Date> serializer2 = createSerializer(Date.class, false, customFormat);
        serializer2.serialize(date1, generator, serializers);
        assertEquals(customFormat.format(date1), generator.getOutputValue().asText());

        serializer2.serialize(date2, generator, serializers);
        assertEquals(customFormat.format(date2), generator.getOutputValue().asText());
    }

    private DateTimeSerializerBase<Date> createSerializer(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<Date>() {
            @Override
            protected DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat format) {
                return new DateTimeSerializerBase<Date>() {
                    @Override
                    protected boolean _timestamp(Date value) {
                        return timestamp != null && timestamp;
                    }

                    @Override
                    protected DateFormat _customFormat() {
                        return format;
                    }

                    @Override
                    protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                        if (customFormat != null) {
                            DateTimeSerializerBaseTest.this._serializeAsString(value, g, provider);
                        } else {
                            super._serializeAsString(value, g, provider);
                        }
                    }

                    @Override
                    public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, JsonFormat.Value format) {
                        return this;
                    }
                };
            }
        }.withFormat(useTimestamp, customFormat);
    }
}