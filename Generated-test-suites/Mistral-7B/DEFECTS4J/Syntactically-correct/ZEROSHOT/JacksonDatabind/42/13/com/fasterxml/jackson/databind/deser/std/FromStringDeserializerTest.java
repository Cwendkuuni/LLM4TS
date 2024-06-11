package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"file.txt\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(File.class);
        File result = mapper.readValue(json, deserializer.getType());
        assertNull(result);
    }

    @Test
    public void testUrlDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"http://example.com\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(URL.class);
        URL result = mapper.readValue(json, deserializer.getType());
        assertNull(result);
    }

    @Test
    public void testUriDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"http://example.com/path?query=value#fragment\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(URI.class);
        URI result = mapper.readValue(json, deserializer.getType());
        URI expected = new URI("http", "example.com", "/path", null, null, null, null, null, null);
        assertNull(result.getFragment());
        assertNull(result.getHost());
        assertNull(result.getPath());
        assertNull(result.getQuery());
        assertNull(result.getScheme());
        assertNull(result.getUserInfo());
        assertEquals(expected.getHostText(), result.getHostText());
        assertEquals(expected.getPath(), result.getPath());
        assertEquals(expected.getRawQuery(), result.getRawQuery());
        assertEquals(expected.getScheme(), result.getScheme());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"java.lang.String\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class);
        Class<?> result = mapper.readValue(json, deserializer.getType());
        assertEquals(String.class, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"java.util.List<java.lang.String>\"]";
        FromStringDeserializer deserializer = new FromStringDeserializer(null);
        JavaType result = mapper.getFactory().constructType(json);
        JavaType expected = mapper.getTypeFactory().constructParametricType(Arrays.asList(String.class), null);
        assertEquals(expected.getRawClass(), result.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"USD\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(Currency.class);
        Currency result = mapper.readValue(json, deserializer.getType());
        Currency expected = Currency.getInstance("USD");
        assertEquals(expected, result);
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"/[a-zA-Z]+\\.{3}[a-zA-Z]{2}\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(Pattern.class);
        Pattern result = mapper.readValue(json, deserializer.getType());
        assertEquals(Pattern.compile(result.pattern()), result);
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"en_US\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(Locale.class);
        Locale result = mapper.readValue(json, deserializer.getType());
        Locale expected = Locale.forLanguageTag("en_US");
        assertEquals(expected, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"UTF-8\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(Charset.class);
        Charset result = mapper.readValue(json, deserializer.getType());
        Charset expected = Charset.forName("UTF-8");
        assertEquals(expected, result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"America/Los_Angeles\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(TimeZone.class);
        TimeZone result = mapper.readValue(json, deserializer.getType());
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        assertEquals(expected, result);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"127.0.0.1\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(InetAddress.class);
        InetAddress result = mapper.readValue(json, deserializer.getType());
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        assertEquals(expected, result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"[255.255.255.255]:80\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(InetSocketAddress.class);
        InetSocketAddress result = mapper.readValue(json, deserializer.getType());
        InetSocketAddress expected = new InetSocketAddress("255.255.255.255", 80);
        assertEquals(expected, result);

        json = "\"[::1]:80\"";
        result = mapper.readValue(json, deserializer.getType());
        expected = new InetSocketAddress("0:0:0:0:0:0:0:1", 80);
        assertEquals(expected, result);
    }

    @Test
    public void testUnsupportedDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"java.util.ArrayList<java.lang.Integer>\"";
        FromStringDeserializer deserializer = new FromStringDeserializer(String.class);
        JsonMappingException ex = assertThrows(JsonMappingException.class, () -> mapper.readValue(json, deserializer.getType()));
        assertEquals("Don't know how to convert embedded Object of type java.util.ArrayList<java.lang.Integer> into java.lang.String", ex.getMessage());
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<>(String.class);
        String json = "\"\"";
        String result = mapper.readValue(json, deserializer.getType());
        assertNull(result);
    }

    @Test
    public void testEmptyStringDeserializationForUri() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<>(URI.class);
        String json = "\"\"";
        URI result = mapper.readValue(json, deserializer.getType());
        assertEquals(URI.create(""), result);
    }
}