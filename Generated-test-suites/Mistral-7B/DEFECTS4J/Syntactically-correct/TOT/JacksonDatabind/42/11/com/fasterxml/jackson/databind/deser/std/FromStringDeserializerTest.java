package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.SerializableStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    private static ObjectMapper mapper;

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new SerializableStringSerializer());
        mapper = new ObjectMapper(module);
    }

    @org.junit.Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        String json = "\"test.txt\"";
        File actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testURLDeserialization() throws IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        String json = "\"http://example.com\"";
        URL actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testURIDeserialization() throws IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        String json = "\"http://example.com\"";
        URI actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testClassDeserialization() throws IOException, InstantiationException, IllegalAccessException {
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        Class<?> expected = String.class;
        String json = "String";
        Class<?> actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testJavaTypeDeserialization() throws IOException {
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType expected = mapper.getTypeFactory().constructType(String.class);
        String json = "java.lang.String";
        JavaType actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        String json = "\"USD\"";
        Currency actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile("[a-z]+");
        String json = "\"[a-z]+\"";
        Pattern actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        String json = "\"en_US\"";
        Locale actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        String json = "\"UTF-8\"";
        Charset actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("America/New_York");
        String json = "\"America/New_York\"";
        TimeZone actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testInetAddressDeserialization() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        String json = "\"192.168.1.1\"";
        InetAddress actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testInetSocketAddressDeserialization() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        String json = "\"localhost:8080\"";
        InetSocketAddress actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_File() throws IOException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertEquals(URI.create(""), actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_Class() throws IOException, InstantiationException, IllegalAccessException {
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        String actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\"")).getValueAs(deserializer.getClass());
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_JavaType() throws IOException {
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_Currency() throws IOException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_Pattern() throws IOException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_Charset() throws IOException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_TimeZone() throws IOException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_InetAddress() throws IOException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization_InetSocketAddress() throws IOException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("\"\""));
        assertNull(actual);
    }

    @org.junit.Test
    public void testInvalidJsonString() throws IOException {
        FromStringDeserializer<String> deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertThrows(JsonMappingException.class, () -> mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance("[1,2,3]")));
    }

    @org.junit.Test
    public void testDeserializationOfEmbeddedObject() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String json = "{\"embedded\": [1, 2, 3]}";
        Object actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertInstanceOf(Object[].class, actual);
    }

    @org.junit.Test
    public void testDeserializationOfEmbeddedObjectNull() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String json = "{\"embedded\": null}";
        Object actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertNull(actual);
    }

    @org.junit.Test
    public void testDeserializationOfEmbeddedObjectNonDeserializableType() throws IOException {
        FromStringDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(Object.class);
        String json = "{\"embedded\": \"non-deserializable\"}";
        Object actual = mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json)).getValueAs(deserializer.getClass());
        assertThrows(JsonMappingException.class, () -> {
            mapper.readerForUpdating(null).readValue(TextNode.class.getDeclaredConstructor().newInstance(json));
        });
    }
}