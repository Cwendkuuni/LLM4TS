package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = JsonMapperFactory.getMapperWithFeatures(
            new JsonMapper.Builder().enable(SerializationFeature.FAIL_ON_EMPTY_BEANS));

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.io.File\",\"path\":\"test.txt\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = (File) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals("test.txt", result.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.net.URL\",\"stringRepresentation\":\"https://test.com\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = (URL) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals("https://test.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.net.URI\",\"stringRepresentation\":\"http://test.com/path/to/file\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals("http://test.com/path/to/file", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.util.Currency\",\"currencyCode\":\"USD\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = (Currency) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.util.regex.Pattern\",\"pattern\":\"\\d{3}\\-\\d{2}-\\d{2}\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = (Pattern) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertNotNull(result.pattern());
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.util.Locale\",\"localeString\":\"en_US\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = (Locale) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.nio.charset.Charset\",\"charsetName\":\"UTF-8\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = (Charset) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.util.TimeZone\",\"timeZoneID\":\"America/Los_Angeles\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = (TimeZone) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInvalidDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.util.Locale\",\"localeString\":\"InvalidLocale\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);       
        try {
            deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
            Assert.fail("Should have thrown an exception");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Failed to deserialize java.util.Locale from String \"InvalidLocale\"", e.getMessage());
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"className\":\"java.net.URI\",\"stringRepresentation\":\"\"}";
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(objectMapper.getFactory().createParser(json), objectMapper.getDeserializationContext());
        Assert.assertEquals(URI.create(""), result);
    }
}