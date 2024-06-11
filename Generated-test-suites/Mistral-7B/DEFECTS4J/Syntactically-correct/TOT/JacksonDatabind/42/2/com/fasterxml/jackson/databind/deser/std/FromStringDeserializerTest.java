package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws JsonMappingException, IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"C:\\test.txt\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<File> deserializer = (FromStringDeserializer<File>) findDeserializer(File.class);
        File result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(File.class, result);
        assertInstanceOf(java.io.File.class, result);
    }

    @Test
    public void testURLDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"http://example.com\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<URL> deserializer = (FromStringDeserializer<URL>) findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(URL.class, result);
        assertInstanceOf(java.net.URL.class, result);
    }

    @Test
    public void testURIDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"http://example.com/path/to/file\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<URI> deserializer = (FromStringDeserializer<URI>) findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(URI.class, result);
        assertInstanceOf(java.net.URI.class, result);
    }

    @Test
    public void testClassDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"java.util.HashMap\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Class<?>> deserializer = (FromStringDeserializer<Class<?>>) findDeserializer(Class.class);
        Class<?> result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(Class.class, result);
        assertInstanceOf(java.lang.Class.class, result);
        assertInstanceOf(Map.class, ClassUtil.getRawType(result));
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"java.util.List<java.lang.String>\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<JavaType> deserializer = (FromStringDeserializer<JavaType>) findDeserializer(JavaType.class);
        JavaType result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(JavaType.class, result);
        assertInstanceOf(com.fasterxml.jackson.databind.JavaType.class, result);
        assertInstanceOf(java.util.List.class, result.getType());
        assertInstanceOf(java.lang.String.class, result.getType().getComponentType());
    }

    @Test
    public void testCurrencyDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"USD\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Currency> deserializer = (FromStringDeserializer<Currency>) findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(Currency.class, result);
        assertInstanceOf(java.util.Currency.class, result);
        assertEquals("USD", result.getCurrencyCode());
    }

    @Test
    public void testPatternDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"/ab(c|def|gh)i$/g\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Pattern> deserializer = (FromStringDeserializer<Pattern>) findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(Pattern.class, result);
        assertInstanceOf(java.util.regex.Pattern.class, result);
    }

    @Test
    public void testLocaleDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"en_US\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Locale> deserializer = (FromStringDeserializer<Locale>) findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(Locale.class, result);
        assertInstanceOf(java.util.Locale.class, result);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testCharsetDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"UTF-8\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<Charset> deserializer = (FromStringDeserializer<Charset>) findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(Charset.class, result);
        assertInstanceOf(java.nio.charset.Charset.class, result);
        assertEquals(StandardCharsets.UTF_8, result);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"UTC\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<TimeZone> deserializer = (FromStringDeserializer<TimeZone>) findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(TimeZone.class, result);
        assertInstanceOf(java.util.TimeZone.class, result);
        assertEquals("UTC", result.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"192.168.1.1\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<InetAddress> deserializer = (FromStringDeserializer<InetAddress>) findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(InetAddress.class, result);
        assertInstanceOf(java.net.InetAddress.class, result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"[192.168.1.1]:8080\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<InetSocketAddress> deserializer = (FromStringDeserializer<InetSocketAddress>) findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);
        assertInstanceOf(InetSocketAddress.class, result);
        assertInstanceOf(java.net.InetSocketAddress.class, result);
        assertEquals("192.168.1.1", result.getAddress().getHostAddress());
        assertEquals(8080, result.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) findDeserializer(String.class);
        String result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testInvalidDeserialization() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("\"invalid\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer<String> deserializer = (FromStringDeserializer<String>) findDeserializer(String.class);
        try {
            deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private static StdDeserializer findDeserializer(Class<?> rawType) {
        return FromStringDeserializer.findDeserializer(rawType);
    }
}