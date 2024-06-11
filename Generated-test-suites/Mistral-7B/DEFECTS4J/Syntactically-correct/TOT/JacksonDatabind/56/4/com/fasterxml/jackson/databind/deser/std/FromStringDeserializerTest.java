package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"/path/to/file.txt\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        File result = deserializer.deserialize(parser, ctxt);
        assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com:8080/path\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer.Std(URL.class);
        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com:8080/path", result.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com:8080/path\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer.Std(URI.class);
        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com:8080/path", result.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"java.util.ArrayList\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Class<?>> deserializer = new FromStringDeserializer.Std(Class.class);
        Class<?> result = deserializer.deserialize(parser, ctxt);
        assertEquals(ArrayList.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"com.example.MyClass\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = mapper.getTypeFactory().constructType("com.example.MyClass");
        FromStringDeserializer<JavaType> deserializer = new FromStringDeserializer.Std(JavaType.class);
        JavaType result = deserializer.deserialize(parser, ctxt);
        assertEquals(javaType, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Currency> deserializer = new FromStringDeserializer.Std(Currency.class);
        Currency result = deserializer.deserialize(parser, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"/abcd/efg\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Pattern> deserializer = new FromStringDeserializer.Std(Pattern.class);
        Pattern result = deserializer.deserialize(parser, ctxt);
        assertEquals(Pattern.compile("/abcd/efg"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer.Std(Locale.class);
        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals(Locale.US, result);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Charset> deserializer = new FromStringDeserializer.Std(Charset.class);
        Charset result = deserializer.deserialize(parser, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"UTC\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<TimeZone> deserializer = new FromStringDeserializer.Std(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, ctxt);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"192.168.0.1\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<InetAddress> deserializer = new FromStringDeserializer.Std(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, ctxt);
        assertEquals(InetAddress.getByName("192.168.0.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"[::1]:8080\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<InetSocketAddress> deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, ctxt);
        InetSocketAddress expected = new InetSocketAddress(InetAddress.getByAddress(new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}), 8080);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        File result = deserializer.deserialize(parser, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidString() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"invalid\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeUnexpectedToken() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("\"[1,2,3]\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<File> deserializer = new FromStringDeserializer.Std(File.class);
        try {
            deserializer.deserialize(parser, ctxt);
            fail();
        } catch (InvalidFormatException e) {
            // expected
        }
    }
}