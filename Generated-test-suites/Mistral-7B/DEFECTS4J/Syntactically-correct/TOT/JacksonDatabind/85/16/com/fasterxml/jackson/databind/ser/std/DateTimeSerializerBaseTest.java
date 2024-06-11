package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeDate_useTimestamp_true() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().findSerializerValue(Date.class);
        long timestamp = 1633022400L; // Thu Jan 01 00:00:00 UTC 2020
        Date date = new Date(timestamp * 1000L);

        DateTimeSerializerBase<Date> serializedSerializer = (DateTimeSerializerBase<Date>) dateSerializer.withFormat(true, null);

        String json = serializedSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        TextNode expectedJson = JsonNodeFactory.instance.textValue("1633022400000");

        assertEquals(expectedJson.asText(), json);
    }

    @Test
    public void testSerializeDate_useTimestamp_false() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        JsonGenerator jsonGenerator = objectMapper.getPolymorphicSerializerProvider().findSerializerValue(Date.class);
        Date date = new Date(System.currentTimeMillis());

        DateTimeSerializerBase<Date> serializedSerializer = (DateTimeSerializerBase<Date>) dateSerializer.withFormat(false, null);

        String json = serializedSerializer.serialize(date, jsonGenerator, objectMapper.getSerializerProvider());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String expectedJson = dateFormat.format(date);

        assertEquals(expectedJson, json);
    }

    @Test
    public void testSerializeDate_customFormat() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setPattern("dd-MM-yyyy");
        jsonFormat.setLocale(Locale.ENGLISH);
        jsonFormat.setTimeZone(TimeZone.getDefault());

        DateTimeSerializerBase<Date> serializedSerializer = (DateTimeSerializerBase<Date>) dateSerializer.withFormat(false, new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()));

        Date date = new Date(System.currentTimeMillis());
        String json = serializedSerializer.serialize(date, objectMapper.getJsonFactory().createGenerator(new ByteArrayOutputStream()), objectMapper.getSerializerProvider()).getOutputAsString();

        String expectedJson = new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()).format(date);

        assertEquals(expectedJson, json);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeDate_nullSerializerProvider() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        dateSerializer.createContextual(null, null);
    }

    @Test
    public void testDeserializeDate_useTimestamp_true() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader dateReader = objectMapper.getPolymorphicDeserializer(Date.class);
        long timestamp = 1633022400L; // Thu Jan 01 00:00:00 UTC 2020

        DateTimeSerializerBase<Date> serializedSerializer = (DateTimeSerializerBase<Date>) dateSerializer.withFormat(true, null);
        String json = "1633022400000";
        Date date = serializedSerializer.deserialize(JsonParser.parseString(json), objectMapper.getDeserializationContext());

        assertEquals(new Date(timestamp * 1000L), date);
    }

    @Test
    public void testDeserializeDate_useTimestamp_false() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader dateReader = objectMapper.getPolymorphicDeserializer(Date.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        DateTimeSerializerBase<Date> deserializeSerializer = (DateTimeSerializerBase<Date>) dateSerializer.withFormat(false, null);
        String json = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(calendar.getTime());
        Date date = deserializeSerializer.deserialize(JsonParser.parseString(json), objectMapper.getDeserializationContext());

        assertEquals(calendar.getTime(), date);
    }

    @Test
    public void testDeserializeDate_customFormat() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setPattern("dd-MM-yyyy");
        jsonFormat.setLocale(Locale.ENGLISH);
        jsonFormat.setTimeZone(TimeZone.getDefault());

        DateTimeSerializerBase<Date> deserializeSerializer = (DateTimeSerializerBase<Date>) dateSerializer.withFormat(false, new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String json = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());

        Date date = deserializeSerializer.deserialize(JsonParser.parseString(json), objectMapper.getDeserializationContext());

        assertEquals(calendar.getTime(), date);
    }
}