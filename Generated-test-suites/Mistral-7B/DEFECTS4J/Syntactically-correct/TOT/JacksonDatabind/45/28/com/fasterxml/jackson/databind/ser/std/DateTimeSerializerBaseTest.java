package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase_useTimestamp() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        SerializerProvider serializers = mapper.getSerializerProvider();

        Date date = new Date();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, gen, serializers);
        gen.flush();

        assertEquals("1612634842000", gen.toString()); // ISO 8601 timestamp representation

        Date deserializedDate = mapper.readValue(gen.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBase_customFormat() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<ZonedDateTime> serializer = new StdSerializers.LocalDateTimeSerializer();

        ZonedDateTime zdt = ZonedDateTime.of(2022, 1, 1, 12, 0, 0, 0, ZoneId.of("Europe/London"));

        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setCurrentLocale(Locale.US);
        serializers.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(zdt, gen, serializers);
        gen.flush();

        assertEquals("2022-01-01T12:00:00.000Z", gen.toString());

        ZonedDateTime deserializedZdt = mapper.readValue(gen.toString(), ZonedDateTime.class);
        assertSame(zdt, deserializedZdt);
    }

    @Test
    public void testDateTimeSerializerBase_emptyValue() {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        Date nullDate = null;
        assertFalse(serializer.isEmpty(nullDate));
        assertFalse(serializer.isEmpty(serializer, nullDate));
    }

    @Test
    public void testDateTimeSerializerBase_emptyValue_withSerializerProvider() {
        DateTimeSerializerBase<Long> serializer = new StdSerializers.LongSerializers.InstantSerializer();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        Long zeroTimestamp = 0L;
        assertFalse(serializer.isEmpty(zeroTimestamp));
        assertFalse(serializer.isEmpty(serializer, zeroTimestamp, serializers));
    }

    @Test
    public void testDateTimeSerializerBase_schema() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Date.class);
        assertEquals("number", schema.getType());
    }
}