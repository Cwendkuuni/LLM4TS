package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class FromStringDeserializerTest extends BaseTest {
    private ObjectMapper mapper;

    @BeforeClass
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(File.class, FromStringDeserializer.Std.findDeserializer(File.class));
        module.addDeserializer(URL.class, FromStringDeserializer.Std.findDeserializer(URL.class));
        module.addDeserializer(URI.class, FromStringDeserializer.Std.findDeserializer(URI.class));
        // ... add deserializers for other supported types here
        mapper = new ObjectMapper(new AnnotationIntrospector());
        mapper.registerModule(module);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"/test/path/to/file.txt\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        File expected = new File("/test/path/to/file.txt");
        File actual = mapper.readValue(inputStream, File.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"http://test.com/path?query=param\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        URI expected = new URI("http", "test.com", "/path", null, "query=param", null);
        URI actual = mapper.readValue(inputStream, URI.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"vnd://some-scheme/test/path?query=param\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        URI expected = new URI("vnd", "some-scheme", "test/path", null, "query=param", null);
        URI actual = mapper.readValue(inputStream, URI.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"EUR\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Currency expected = Currency.getInstance("EUR");
        Currency actual = mapper.readValue(inputStream, Currency.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"en_US\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Locale expected = new Locale("en", "US");
        Locale actual = mapper.readValue(inputStream, Locale.class);
        assertEquals(actual, expected);

        json = "{\"value\": \"ja_JP_jp\"}";
        inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        expected = new Locale("ja", "JP", "jp");
        actual = mapper.readValue(inputStream, Locale.class);
        assertEquals(actual, expected);

        json = "{\"value\": \"es_ES_ES\"}";
        inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        expected = new Locale("es", "ES", "ES");
        actual = mapper.readValue(inputStream, Locale.class);
        assertEquals(actual, expected);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"America/New_York\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        TimeZone expected = TimeZone.getTimeZone("America/New_York");
        TimeZone actual = mapper.readValue(inputStream, TimeZone.class);
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testInvalidDeserialization() throws IOException, JsonProcessingException {
        String json = "{\"value\": \"invalid\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        mapper.readValue(inputStream, InvalidType.class);
    }

    // Add tests here for other supported types, if necessary

    private static class InvalidType {
    }
}