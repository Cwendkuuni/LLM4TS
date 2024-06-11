package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(File.class);
        JsonNode node = mapper.readTree("\"/test/file.txt\"");
        File result = (File) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("/test/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URL.class);
        JsonNode node = mapper.readTree("\"https://www.google.com\"");
        URL result = (URL) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("https://www.google.com", result.toString());
    }

    @Test
    public void testUriDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonNode node = mapper.readTree("\"https://www.google.com\"");
        URI result = (URI) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("https://www.google.com", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(String.class);
        JsonNode node = mapper.readTree("\"java.lang.String\"");
        String result = (String) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JsonNode node = mapper.readTree("\"com.fasterxml.jackson.databind.JavaType\"");
        JavaType result = (JavaType) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(JavaType.class, result.getClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Currency.class);
        JsonNode node = mapper.readTree("\"EUR\"");
        Currency result = (Currency) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Currency.getInstance("EUR"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Pattern.class);
        JsonNode node = mapper.readTree("\"/\\w+\\.txt\"");
        Pattern result = (Pattern) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("/\\w+\\.txt", result.pattern().toString());
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Locale.class);
        JsonNode node = mapper.readTree("\"en_US\"");
        Locale result = (Locale) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Locale.US, result);

        node = mapper.readTree("\"en_US_POSIX\"");
        result = (Locale) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Locale.US, result);

        node = mapper.readTree("\"en_GB\"");
        result = (Locale) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Locale.UK, result);

        node = mapper.readTree("\"en_GB_POSIX\"");
        result = (Locale) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Locale.UK, result);

        node = mapper.readTree("\"en_GB_POSIX.en_US.UTF-8\"");
        result = (Locale) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(Charset.class);
        JsonNode node = mapper.readTree("\"UTF-8\"");
        Charset result = (Charset) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        JsonNode node = mapper.readTree("\"America/Los_Angeles\"");
        TimeZone result = (TimeZone) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        JsonNode node = mapper.readTree("\"192.168.0.1\"");
        InetAddress result = (InetAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("192.168.0.1", result.getHostAddress());

        node = mapper.readTree("\"127.0.0.1\"");
        result = (InetAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("127.0.0.1", result.getHostAddress());

        node = mapper.readTree("\"123.123.123.123\"");
        result = (InetAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("123.123.123.123", result.getHostAddress());

        node = mapper.readTree("\"256.1.1.1\"");
        result = (InetAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNull(result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        JsonNode node = mapper.readTree("\"192.168.0.1:80\"");
        InetSocketAddress result = (InetSocketAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("192.168.0.1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        node = mapper.readTree("\"::1:80\"");
        result = (InetSocketAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("::1", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());

        node = mapper.readTree("\"[2001:db8:0:1234:0:5678:0:1:1234]\"");
        result = (InetSocketAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("2001:db8:0:1234:0:5678:0:1:1234", result.getAddress().getHostAddress());
        assertEquals(1234, result.getPort());

        node = mapper.readTree("\"[2001:db8:0:1234:0:5678:0:1:1234]:80\"");
        result = (InetSocketAddress) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("2001:db8:0:1234:0:5678:0:1:1234", result.getAddress().getHostAddress());
        assertEquals(80, result.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        DeserializationContext ctxt = mapper.getDeserializationContext();

        FromStringDeserializer serializer = FromStringDeserializer.findDeserializer(URI.class);
        JsonNode node = mapper.readTree("\"\"");
        URI result = (URI) serializer.deserialize(mapper.getFactory().createParser(node), ctxt);
        assertNotNull(result);
        assertEquals("", result.toString());
    }
}