package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDateSerializationWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(Date.class, false, null);
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        Date date = new Date();

        dateSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedDate = objectMapper.readValue(jsonParser, Date.class);

        DateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date expectedDate = expectedFormat.parse("01-01-2022 12:00:00 PM UTC");

        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testDateSerializationWithDefaultFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(Date.class, false, null);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        Date date = new Date();

        dateSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedDate = objectMapper.readValue(jsonParser, Date.class);

        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateSerializationWithUseTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(Date.class, true, null);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        Date date = new Date();

        dateSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedDate = objectMapper.readValue(jsonParser, Long.class);

        assertEquals(date.getTime(), deserializedDate.longValue());
    }

    @Test
    public void testDateSerializationWithCustomFormatAndLocale() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(Date.class, false, new SimpleDateFormat("dd/MM/yyyy"));

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        Date date = new Date();

        dateSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedDate = objectMapper.readValue(jsonParser, Date.class);

        DateFormat expectedFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        Date expectedDate = expectedFormat.parse("01/01/2022");

        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testDateSerializationWithCustomFormatAndTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z"));

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        Date date = new Date();

        dateSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedDate = objectMapper.readValue(jsonParser, Date.class);

        DateFormat expectedFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Date expectedDate = expectedFormat.parse("01-01-2022 11:00:00 AM Europe/London");

        assertEquals(expectedDate, deserializedDate);
    }

    @Test
    public void testZonedDateTimeSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new StdDateSerializer(ZonedDateTime.class, false, null);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        zonedDateTimeSerializer.serialize(zonedDateTime, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedZonedDateTime = objectMapper.readValue(jsonParser, ZonedDateTime.class);

        assertEquals(zonedDateTime, deserializedZonedDateTime);
    }

    @Test
    public void testZonedDateTimeSerializationWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new StdDateSerializer(ZonedDateTime.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        zonedDateTimeSerializer.serialize(zonedDateTime, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedZonedDateTime = objectMapper.readValue(jsonParser, ZonedDateTime.class);

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        ZonedDateTime expectedZonedDateTime = expectedFormat.toZonedDateTime();

        assertEquals(expectedZonedDateTime, deserializedZonedDateTime);
    }

    @Test
    public void testZonedDateTimeSerializationWithUseTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<ZonedDateTime> zonedDateTimeSerializer = new StdDateSerializer(ZonedDateTime.class, true, null);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        zonedDateTimeSerializer.serialize(zonedDateTime, jsonGenerator, objectMapper.getSerializerProvider());
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        Object deserializedZonedDateTime = objectMapper.readValue(jsonParser, Long.class);

        assertEquals(zonedDateTime.toInstant().toEpochMilli(), deserializedZonedDateTime.longValue());
    }
}