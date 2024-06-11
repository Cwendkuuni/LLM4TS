package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase_useTimestamp() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Long timestamp = 1618435721000L; // 19-Jan-2021 12:52:01 UTC
        String json = writer.with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(timestamp);
        JsonParser parser = mapper.parser();
        Long deserialized = parser.parseValueAsLong(json);
        assertEquals(timestamp, deserialized);
    }

    @Test
    public void testDateTimeSerializerBase_customFormat_String() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(date);
        JsonParser parser = mapper.parser();
        Date deserialized = parser.parseValueAsDate(json);
        assertEquals(date, deserialized);
    }

    @Test
    public void testDateTimeSerializerBase_customFormat_LocaleAndTimeZone() throws IOException, JsonMappingException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z", Locale.US, TimeZone.getTimeZone("UTC")));
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(date);
        JsonParser parser = mapper.parser();
        parser.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        parser.setSerializationInclusion(JsonParser.Feature.FAIL_ON_EMPTY_BEANS, false);
        JsonNode node = parser.parse(json);
        Date deserialized = mapper.readerForUpdating(new Date()).readValue(node);
        assertEquals(date, deserialized);
    }

    @Test
    public void testDateTimeSerializerBase_customFormat_JsonFormat() throws IOException, JsonMappingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm:ss z")
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(date);
        JsonParser parser = mapper.parser();
        Date deserialized = parser.parseValueAsDate(json);
        assertEquals(date, deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBase_customFormat_InvalidPattern() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss zz")); // double z
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(date);
        JsonParser parser = mapper.parser();
        parser.parse(json);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBase_customFormat_NullFormat() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(date);
        JsonParser parser = mapper.parser();
        parser.parse(json);
    }

    @Test
    public void testDateTimeSerializerBase_serializeAsString() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        JsonSerializer<Date> jsonSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);

        String json = writer.writeValueAsString(jsonSerializer, date);
        JsonParser parser = mapper.parser();
        Date deserialized = parser.parseValueAsDate(json);
        assertEquals(date, deserialized);
    }

    @Test
    public void testDateTimeSerializerBase_reusedCustomFormat() throws IOException, JsonMappingException, InterruptedException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(date);
        JsonParser parser = mapper.parser();
        Date deserialized = parser.parseValueAsDate(json);

        SimpleDateFormat formatter = (SimpleDateFormat) serializer._reusedCustomFormat.get();
        SimpleDateFormat clonedFormatter = (SimpleDateFormat) formatter.clone();

        assertEquals(formatter.format(date), clonedFormatter.format(date));
    }

    @Test
    public void testDateTimeSerializerBase_withFormat_Timestamp() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Long> base = new DateTimeSerializerBase<Long>(Long.TYPE, null, null);
        DateTimeSerializerBase<Long> withTimestamp = base.withFormat(true, null);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Long timestamp = 1618435721000L; // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(timestamp);
        JsonParser parser = mapper.parser();
        Long deserialized = parser.parseValueAsLong(json);
        assertEquals(timestamp, deserialized);
    }

    @Test
    public void testDateTimeSerializerBase_withFormat_CustomFormat() throws IOException, JsonMappingException {
        DateTimeSerializerBase<Date> base = new DateTimeSerializerBase<Date>(Date.class, false, null);
        DateTimeSerializerBase<Date> withFormat = base.withFormat(false, new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z"));

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Date date = new Date(1618435721000L); // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(withFormat, date);
        JsonParser parser = mapper.parser();
        Date deserialized = parser.parseValueAsDate(json);
        assertEquals(date, deserialized);
    }

    @Test
    public void testDateTimeSerializerBase_SerializationFeature_WRITE_DATES_AS_TIMESTAMPS() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        Long timestamp = 1618435721000L; // 19-Jan-2021 12:52:01 UTC
        String json = writer.writeValueAsString(timestamp);
        JsonParser parser = mapper.parser();
        Long deserialized = parser.parseValueAsLong(json);
        assertEquals(timestamp, deserialized);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JsonSerializer<Long> jsonSerializer = mapper.getSerializerProvider().findValueSerializer(Long.class);
        assertEquals("\"1618435721000\"", jsonSerializer.serializeAsString(timestamp));
    }
}