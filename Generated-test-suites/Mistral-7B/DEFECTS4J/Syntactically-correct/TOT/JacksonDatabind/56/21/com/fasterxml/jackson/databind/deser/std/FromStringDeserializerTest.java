package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException {
        String json = "{\"@class\":\"java.io.File\",\"value\":\"test.txt\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<File> deserializer = new Std.StdFile();
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals(new File("test.txt"), result);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        String json = "{\"@class\":\"java.net.URL\",\"value\":\"http://example.com\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<URL> deserializer = new Std.StdURL();
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        String json = "{\"@class\":\"java.net.URI\",\"value\":\"http://example.com\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<URI> deserializer = new Std.StdURI();
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        String json = "{\"@class\":\"java.lang.String\",\"value\":\"java.util.Date\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.configure(DeserializationFeature.FAIL_ON_Empty_BEANS, false);

        FromStringDeserializer<Class<?>> deserializer = new Std.StdClass();
        Class<?> result = deserializer.deserialize(parser, ctxt);
        assertEquals(Date.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "{\"@class\":\"com.fasterxml.jackson.databind.JsonNode\",\"value\":\"null\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<JavaType> deserializer = new Std.StdJavaType();
        JavaType result = deserializer.deserialize(parser, ctxt);
        TypeFactory typeFactory = ctxt.getTypeFactory();
        assertEquals(typeFactory.constructMapType(Map.class, String.class, JsonNode.class), result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "{\"@class\":\"java.util.Currency\",\"value\":\"USD\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Currency> deserializer = new Std.StdCurrency();
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "{\"@class\":\"java.util.regex.Pattern\",\"value\":\".*\\.txt\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Pattern> deserializer = new Std.StdPattern();
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertEquals(Pattern.compile(".*\\.txt"), result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "{\"@class\":\"java.util.Locale\",\"value\":\"en_US_POSIX\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Locale> deserializer = new Std.StdLocale();
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.forLanguageTag("en_US_POSIX"), result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "{\"@class\":\"java.nio.charset.Charset\",\"value\":\"UTF-8\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Charset> deserializer = new Std.StdCharset();
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "{\"@class\":\"java.util.TimeZone\",\"value\":\"America/Los_Angeles\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<TimeZone> deserializer = new Std.StdTimeZone();
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInvalidClassDeserialization() throws IOException {
        String json = "{\"@class\":\"java.lang.Integer\",\"value\":\"java.util.Date\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Object> deserializer = new Std();
        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Don't know how to convert embedded Object of type java.util.Date into java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<String> deserializer = new Std<String>();
        String result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        String json = "null";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Object> deserializer = new Std<>();
        Object result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testEmptyClassDeserialization() throws IOException {
        String json = "{}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Object> deserializer = new Std<>();
        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a recognized Java class", e.getMessage());
        }
    }

    @Test
    public void testInvalidJsonString() throws IOException {
        String json = "{\"@class\":\"java.lang.Integer\",\"value\":\"java.util.Date\"{";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Object> deserializer = new Std<>();
        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Cannot construct instance of `java.lang.Integer`", e.getMessage());
        }
    }

    @Test
    public void testMalformedLocaleString() throws IOException {
        String json = "{\"@class\":\"java.util.Locale\",\"value\":\"en_US_POSIX_invalid\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Locale> deserializer = new Std.StdLocale();
        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Locale 'en_US_POSIX_invalid' is not valid", e.getMessage());
        }
    }

    @Test
    public void testMalformedTimeZoneString() throws IOException {
        String json = "{\"@class\":\"java.util.TimeZone\",\"value\":\"America/Los_Angeles_invalid\"}";
        JsonParser parser = new JsonFactory().createParser(mapper.textToString(json));
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<TimeZone> deserializer = new Std.StdTimeZone();
        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Timezone 'America/Los_Angeles_invalid' is not valid", e.getMessage());
        }
    }
}