package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.BaseTest;
import com.fasterxml.jackson.databind.node.TextNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest extends BaseTest {

    private ObjectMapper mapper;

    @BeforeMethod
    public void beforeMethod() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = createMyJsonGenerator(provider);

        Date date = new Date(System.currentTimeMillis());
        dateSerializer.serialize(date, gen, provider);

        JsonNode result = mapper.readTree(gen.getOutputStream());
        TextNode expected = textNode("2022-03-26T14:53:32.123Z");
        assertEquals(result, expected);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String expectedString = dateFormat.format(date);
        assertEquals(result.asText(), expectedString);
    }

    @Test
    public void testCustomFormatSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer()
                .withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH) {
                    {
                        setTimeZone(TimeZone.getTimeZone("UTC"));
                    }
                });
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = createMyJsonGenerator(provider);

        Date date = new Date(System.currentTimeMillis());
        dateSerializer.serialize(date, gen, provider);

        JsonNode result = mapper.readTree(gen.getOutputStream());
        TextNode expected = textNode("26-03-2022 14:53:32");
        assertEquals(result, expected);

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String expectedString = customFormat.format(date);
        assertEquals(result.asText(), expectedString);
    }

    @Test
    public void testSerializationWithTimestampFlag() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer()
                .withFormat(true, null);
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = createMyJsonGenerator(provider);

        Date date = new Date(System.currentTimeMillis());
        dateSerializer.serialize(date, gen, provider);

        JsonNode result = mapper.readTree(gen.getOutputStream());
        TextNode expected = textNode("1647459212000");
        assertEquals(result, expected);

        assertEquals(result.asLongValue(), date.getTime());
    }

    @Test
    public void testSerializationWithNullValue() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = createMyJsonGenerator(provider);

        dateSerializer.serialize(null, gen, provider);

        JsonNode result = mapper.readTree(gen.getOutputStream());
        assertNull(result);
    }
}