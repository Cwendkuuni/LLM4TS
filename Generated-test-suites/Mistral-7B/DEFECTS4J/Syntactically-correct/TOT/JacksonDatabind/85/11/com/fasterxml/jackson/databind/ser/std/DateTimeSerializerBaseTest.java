package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializingDateWithDefaultFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        JsonNode jsonNode = mapper.valueToTree(new Date());
        Assert.assertEquals("\"2023-03-21T00:00:00.000Z\"", jsonNode.toString());
    }

    @Test
    public void testSerializingDateWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer().withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        JsonNode jsonNode = mapper.valueToTree(new Date());
        Assert.assertEquals("\"22-03-2023 10:10:10\"", jsonNode.toString());
    }

    @Test
    public void testSerializingDateWithUseTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer().withFormat(Boolean.TRUE, null);
        JsonNode jsonNode = mapper.valueToTree(new Date());
        Assert.assertEquals("1679452800000", jsonNode.toString());
    }

    @Test
    public void testSerializingDateWithJsonFormatAnnotation() throws JsonProcessingException, IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        JsonNode jsonNode = mapper.valueToTree(new Date());
        Assert.assertEquals("\"22-03-2023 10:10:10\"", jsonNode.toString());
    }

    @Test
    public void testDeserializingDateWithDefaultFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        Date date = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"2023-03-21T00:00:00.000Z\""), Date.class);
        Assert.assertEquals(new Date(1679452800000L), date);
    }

    @Test
    public void testDeserializingDateWithCustomFormat() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer().withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        Date date = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"22-03-2023 10:10:10\""), Date.class);
        Assert.assertEquals(new Date(1679452800000L), date);
    }

    @Test
    public void testDeserializingDateWithUseTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer().withFormat(Boolean.TRUE, null);
        Date date = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("1679452800000"), Date.class);
        Assert.assertEquals(new Date(1679452800000L), date);
    }

    @Test
    public void testDeserializingDateWithJsonFormatAnnotation() throws JsonProcessingException, ParseException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        Date date = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"22-03-2023 10:10:10\""), Date.class);
        Assert.assertEquals(new Date(1679452800000L), date);
    }

    @Test
    public void testSerializingInstantWithDefaultFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer();
        JsonNode jsonNode = mapper.valueToTree(Instant.now());
        Assert.assertEquals("\"2023-03-21T12:13:14.567Z\"", jsonNode.toString());
    }

    @Test
    public void testSerializingInstantWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer().withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS"));
        JsonNode jsonNode = mapper.valueToTree(Instant.now());
        Assert.assertEquals("\"22-03-2023 12:13:14.567\"", jsonNode.toString());
    }

    @Test
    public void testSerializingInstantWithUseTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer().withFormat(Boolean.TRUE, null);
        JsonNode jsonNode = mapper.valueToTree(Instant.now());
        long instantAsLong = jsonNode.asLong();
        Assert.assertTrue(Math.abs(instantAsLong - Instant.now().getEpochSecond()) <= 1);
    }

    @Test
    public void testDeserializingInstantWithDefaultFormat() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer();
        Instant instant = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"2023-03-21T12:13:14.567Z\""), Instant.class);
        Assert.assertEquals(Instant.now(), instant);
    }

    @Test
    public void testDeserializingInstantWithCustomFormat() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer().withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS"));
        Instant instant = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"22-03-2023 12:13:14.567\""), Instant.class);
        Assert.assertEquals(Instant.now(), instant);
    }

    @Test
    public void testDeserializingInstantWithUseTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer().withFormat(Boolean.TRUE, null);
        Instant instant = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("1679452865000"), Instant.class);
        Assert.assertEquals(Instant.now(), instant);
    }

    @Test
    public void testDeserializingInstantWithJsonFormatAnnotation() throws JsonProcessingException, ParseException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
        DateTimeSerializerBase<Instant> instantSerializer = new StdInstantSerializer();
        Instant instant = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"22-03-2023 12:13:14.567\""), Instant.class);
        Assert.assertEquals(Instant.now(), instant);
    }

    @Test
    public void testSerializingWithNullValue() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        dateTimeSerializer.serialize(null, jsonGenerator, mapper);
        Assert.assertEquals("null", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDeserializingWithNullValue() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer();
        Date date = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("null"), Date.class);
        Assert.assertNull(date);
    }

    @Test
    public void testSerializingWithInvalidFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer().withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS"));
        try {
            JsonNode jsonNode = mapper.valueToTree(new Date());
        } catch (JsonProcessingException e) {
            Assert.assertEquals("Invalid format: \"dd-MM-yyyy HH:mm:ss:SSS\" for SerializationInput(class [B, size = 42)", e.getMessage());
        }
    }

    @Test
    public void testDeserializingWithInvalidFormat() throws JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> dateTimeSerializer = new StdDateSerializer().withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS"));
        try {
            Date date = mapper.readValue(TextNode.class.getDeclaredConstructor().newInstance("\"22-03-2023 12:13:14.567.123\""), Date.class);
        } catch (JsonProcessingException e) {
            Assert.assertEquals("Cannot deserialize value of type [simple type, class java.util.Date] from JSON String \"[\"22-03-2023 12:13:14.567.123\"]\": Failed to deserialize Date value: Unexpected token (START_ARRAY) at [Source: (StringReader); line: 1, column: 1]", e.getMessage());
        }
    }
}