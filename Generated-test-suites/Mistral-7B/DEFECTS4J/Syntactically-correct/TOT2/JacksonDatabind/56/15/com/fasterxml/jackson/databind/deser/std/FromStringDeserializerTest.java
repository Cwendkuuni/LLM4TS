package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(File.class, FromStringDeserializer.findDeserializer(File.class));
        module.addDeserializer(URL.class, FromStringDeserializer.findDeserializer(URL.class));
        module.addDeserializer(URI.class, FromStringDeserializer.findDeserializer(URI.class));
        module.addDeserializer(Class.class, FromStringDeserializer.findDeserializer(Class.class));
        module.addDeserializer(JavaType.class, FromStringDeserializer.findDeserializer(JavaType.class));
        module.addDeserializer(Currency.class, FromStringDeserializer.findDeserializer(Currency.class));
        module.addDeserializer(Pattern.class, FromStringDeserializer.findDeserializer(Pattern.class));
        module.addDeserializer(Locale.class, FromStringDeserializer.findDeserializer(Locale.class));
        module.addDeserializer(Charset.class, FromStringDeserializer.findDeserializer(Charset.class));
        module.addDeserializer(TimeZone.class, FromStringDeserializer.findDeserializer(TimeZone.class));
        module.addDeserializer(InetAddress.class, FromStringDeserializer.findDeserializer(InetAddress.class));
        module.addDeserializer(InetSocketAddress.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFileDeserialization() throws IOException, URISyntaxException {
        String json = "\"/path/to/file.txt\"";
        File file = objectMapper.readValue(json, File.class);
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException, URISyntaxException {
        String json = "\"http://example.com/path\"";
        URL url = objectMapper.readValue(json, URL.class);
        assertEquals("http://example.com/path", url.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException, URISyntaxException {
        String json = "\"http://example.com/path?query=param\"";
        URI uri = objectMapper.readValue(json, URI.class);
        assertEquals("http", uri.getScheme());
        assertEquals("/path", uri.getPath());
        assertEquals("query=param", uri.getQuery());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        String json = "\"java.util.HashMap\"";
        Class<?> clazz = objectMapper.readValue(json, Class.class);
        assertTrue(clazz.isInstance(new HashMap<>()));
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "\"com.example.MyCustomList<java.lang.String>\"".replaceAll("\\$", "");
        JavaType javaType = objectMapper.readValue(json, JavaType.class);
        assertEquals(MyCustomList.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "\"USD\"";
        Currency currency = objectMapper.readValue(json, Currency.class);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "\"/[abc]/\"".replaceAll("\\$", "");
        Pattern pattern = objectMapper.readValue(json, Pattern.class);
        String regex = pattern.pattern();
        assertEquals("/[abc]/", regex);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "\"en_US\"";
        Locale locale = objectMapper.readValue(json, Locale.class);
        assertEquals("en_US", locale.toString());
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "\"UTF-8\"";
        Charset charset = objectMapper.readValue(json, Charset.class);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "\"America/Los_Angeles\"";
        TimeZone timeZone = objectMapper.readValue(json, TimeZone.class);
        assertEquals("America/Los_Angeles", timeZone.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        String json = "\"192.168.0.1\"";
        InetAddress inetAddress = objectMapper.readValue(json, InetAddress.class);
        assertEquals("192.168.0.1", inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        String json = "\"[255.255.255.255]:80\"";
        InetSocketAddress inetSocketAddress = objectMapper.readValue(json, InetSocketAddress.class);
        assertEquals("[255.255.255.255]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        assertEquals(null, objectMapper.readValue(json, File.class));
        assertEquals(null, objectMapper.readValue(json, URL.class));
        assertEquals(null, objectMapper.readValue(json, URI.class));
        assertEquals(null, objectMapper.readValue(json, Class.class));
        assertEquals(null, objectMapper.readValue(json, JavaType.class));
        assertEquals(null, objectMapper.readValue(json, Currency.class));
        assertEquals(null, objectMapper.readValue(json, Pattern.class));
        assertEquals(Locale.ROOT, objectMapper.readValue(json, Locale.class));
        assertEquals(Charset.defaultCharset(), objectMapper.readValue(json, Charset.class));
        assertEquals(TimeZone.getDefault(), objectMapper.readValue(json, TimeZone.class));
        assertEquals(null, objectMapper.readValue(json, InetAddress.class));
        assertEquals(null, objectMapper.readValue(json, InetSocketAddress.class));
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        String json = "\"InvalidType\"";
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, File.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, URL.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, URI.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, Class.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, JavaType.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, Currency.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, Pattern.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, Locale.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, Charset.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, TimeZone.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, InetAddress.class));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, InetSocketAddress.class));
    }
}