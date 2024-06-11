package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private static ObjectMapper objectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(File.class, FromStringDeserializer.Std.findDeserializer(File.class));
        module.addDeserializer(URL.class, FromStringDeserializer.Std.findDeserializer(URL.class));
        module.addDeserializer(URI.class, FromStringDeserializer.Std.findDeserializer(URI.class));
        module.addDeserializer(Class.class, FromStringDeserializer.Std.findDeserializer(Class.class));
        module.addDeserializer(JavaType.class, FromStringDeserializer.Std.findDeserializer(JavaType.class));
        module.addDeserializer(Currency.class, FromStringDeserializer.Std.findDeserializer(Currency.class));
        module.addDeserializer(Pattern.class, FromStringDeserializer.Std.findDeserializer(Pattern.class));
        module.addDeserializer(Locale.class, FromStringDeserializer.Std.findDeserializer(Locale.class));
        module.addDeserializer(Charset.class, FromStringDeserializer.Std.findDeserializer(Charset.class));
        module.addDeserializer(TimeZone.class, FromStringDeserializer.Std.findDeserializer(TimeZone.class));
        module.addDeserializer(InetAddress.class, FromStringDeserializer.Std.findDeserializer(InetAddress.class));
        module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.Std.findDeserializer(InetSocketAddress.class));
        return new ObjectMapper(module);
    }

    @Test
    public void testDeserializeFile() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"path/to/file.txt\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.Std.findDeserializer(File.class);
        File file = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testDeserializeURL() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"http://example.com\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.Std.findDeserializer(URL.class);
        URL url = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("http://example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"http://example.com/path/to/resource\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.Std.findDeserializer(URI.class);
        URI uri = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("http", uri.getScheme());
        assertEquals("/path/to/resource", uri.getPath());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"java.lang.String\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.Std.findDeserializer(Class.class);
        Class<?> clazz = deserializer.deserialize(jsonParser, ctxt);
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"com.example.MyClass\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.Std.findDeserializer(JavaType.class);
        JavaType javaType = deserializer.deserialize(jsonParser, ctxt);
        TypeFactory typeFactory = ctxt.getTypeFactory();
        assertEquals(typeFactory.constructType(MyClass.class), javaType);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"EUR\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.Std.findDeserializer(Currency.class);
        Currency currency = deserializer.deserialize(jsonParser, ctxt);
        assertEquals(Currency.EUR, currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"/^[a-z]+$/i\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.Std.findDeserializer(Pattern.class);
        Pattern pattern = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("/^[a-z]+$/i", pattern.pattern());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"en_US\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.Std.findDeserializer(Locale.class);
        Locale locale = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"UTF-8\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.Std.findDeserializer(Charset.class);
        Charset charset = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"UTC\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.Std.findDeserializer(TimeZone.class);
        TimeZone timeZone = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"192.168.1.1\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.Std.findDeserializer(InetAddress.class);
        InetAddress inetAddress = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("192.168.1.1", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"[192.168.1.1]:8080\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.Std.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = deserializer.deserialize(jsonParser, ctxt);
        assertEquals("192.168.1.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeNull() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("null");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.Std.findDeserializer(Object.class);
        assertNull(deserializer.deserialize(jsonParser, ctxt));
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"\"");
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.Std.findDeserializer(Object.class);
        Object result = deserializer.deserialize(jsonParser, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeInvalidType() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser("\"[1, 2, 3]\"").setCodec(objectMapper().getFactory());
        DeserializationContext ctxt = objectMapper().getDeserializationContext();
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.Std.findDeserializer(Object.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(jsonParser, ctxt));
    }
}