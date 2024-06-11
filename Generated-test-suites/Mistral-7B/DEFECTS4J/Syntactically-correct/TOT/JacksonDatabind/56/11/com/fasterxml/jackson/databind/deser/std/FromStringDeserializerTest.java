package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.deser.std.StdFileDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdUriDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdLocaleDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdPatternDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdCharsetDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdTimeZoneDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdInetAddressDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdInetSocketAddressDeserializer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = JsonMapper.builder().build();

    @Test
    public void testDeserializeFile() throws IOException {
        StdFileDeserializer deserializer = new StdFileDeserializer(File.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        File file = new File("test.txt");
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(file), ctxt);
        assertEquals(file, result);
    }

    @Test
    public void testDeserializeUrl() throws IOException, URISyntaxException {
        StdUriDeserializer deserializer = new StdUriDeserializer(URI.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        URI uri = new URI("http://example.com/test.html");
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(uri.toString()), ctxt);
        assertEquals(uri, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        StdLocaleDeserializer deserializer = new StdLocaleDeserializer(Locale.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("en_US").asText()), ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        StdPatternDeserializer deserializer = new StdPatternDeserializer(Pattern.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("\\d+").asText()), ctxt);
        assertEquals(Pattern.compile("\\d+"), result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        StdCharsetDeserializer deserializer = new StdCharsetDeserializer(Charset.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("US-ASCII").asText()), ctxt);
        assertEquals(Charset.forName("US-ASCII"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        StdTimeZoneDeserializer deserializer = new StdTimeZoneDeserializer(TimeZone.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("UTC").asText()), ctxt);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        StdInetAddressDeserializer deserializer = new StdInetAddressDeserializer(InetAddress.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("192.168.1.1").asText()), ctxt);
        InetAddress inetAddress = (InetAddress) result;
        assertEquals("192.168.1.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        StdInetSocketAddressDeserializer deserializer = new StdInetSocketAddressDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("[255.255.255.255]:80").asText()), ctxt);
        InetSocketAddress inetSocketAddress = (InetSocketAddress) result;
        assertEquals("255.255.255.255", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        String className = "java.util.Date";
        StdDeserializer<Class<?>> deserializer = new StdDeserializer<Class<?>>(Class.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode(className).asText()), ctxt);
        TypeFactory typeFactory = ctxt.getTypeFactory();
        Class<?> expectedClass = typeFactory.constructClass(className);
        assertEquals(expectedClass, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        String typeName = "java.util.List<java.lang.String>";
        StdDeserializer<JavaType> deserializer = new StdDeserializer<JavaType>(JavaType.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode(typeName).asText()), ctxt);
        JavaType expectedType = objectMapper.getTypeFactory().constructType(typeName);
        assertEquals(expectedType, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        String currencyCode = "USD";
        StdDeserializer<Currency> deserializer = new StdDeserializer<Currency>(Currency.class);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode(currencyCode).asText()), ctxt);
        Currency expectedCurrency = Currency.getInstance(currencyCode);
        assertEquals(expectedCurrency, result);
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        Std<Object> deserializer = new Std(Object.class, Std.STD_STRING);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("").asText()), ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithInvalidString() throws IOException {
        Std<Object> deserializer = new Std(Object.class, Std.STD_STRING);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("invalid").asText()), ctxt);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("invalid").asText()), ctxt));
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        Std<Object> deserializer = new Std(Object.class, Std.STD_STRING);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        T result = deserializer.deserialize(objectMapper.getFactory().createParser(new TextNode(null).asText()), ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmbeddedObject_Null() throws IOException {
        Std<Object> deserializer = new Std(Object.class, Std.STD_STRING);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object ob = null;
        T result = deserializer.deserializeEmbedded(ob, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeEmbeddedObject_InvalidClass() throws IOException {
        Std<Object> deserializer = new Std(Object.class, Std.STD_STRING);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Object ob = new Object();
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> deserializer.deserializeEmbedded(ob, ctxt));
        assertEquals("Don't know how to convert embedded Object of type %s into %s", ob.getClass().getName(), Object.class.getName(), exception.getMessage());
    }
}