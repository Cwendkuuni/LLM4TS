package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        String jsonString = "\"/path/to/file.txt\"";
        File file = mapper.readerForUpdating(null).readValueAs(File.class, deserializer);
        assertNotNull(file);
        assertEquals("/path/to/file.txt", file.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String jsonString = "\"https://www.example.com\"";
        URL url = mapper.readerForUpdating(null).readValueAs(URL.class, deserializer);
        assertNotNull(url);
        assertEquals("https://www.example.com", url.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String jsonString = "\"vnd.example:/path/to/resource\"";
        URI uri = mapper.readerForUpdating(null).readValueAs(URI.class, deserializer);
        assertNotNull(uri);
        assertEquals("vnd.example:/path/to/resource", uri.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        String jsonString = "\"java.util.ArrayList\"";
        Class<?> clazz = mapper.readerForUpdating(null).readValueAs(Class.class, deserializer);
        assertNotNull(clazz);
        assertEquals(ArrayList.class, clazz);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidClassDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Class<?>> deserializer = FromStringDeserializer.findDeserializer(Class.class);
        String jsonString = "\"com.nonExistentPackage.NonExistentClass\"";
        mapper.readerForUpdating(null).readValueAs(Class.class, deserializer);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String jsonString = "\"com.example:java.util.List<java.lang.String>\"" ;
        JavaType javaType = mapper.readerForUpdating(null).readValueAs(JavaType.class, deserializer);
        assertNotNull(javaType);
        assertEquals(TypeFactory.defaultInstance().constructType(java.util.List.class, String.class), javaType);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String jsonString = "\"USD\"";
        Currency currency = mapper.readerForUpdating(null).readValueAs(Currency.class, deserializer);
        assertNotNull(currency);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCurrencyDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String jsonString = "\"EUR\"99\"";
        mapper.readerForUpdating(null).readValueAs(Currency.class, deserializer);
    }

    @Test
    public void testPatternDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String jsonString = "\"/^[a-zA-Z0-9_]+$/\"";
        Pattern pattern = mapper.readerForUpdating(null).readValueAs(Pattern.class, deserializer);
        assertNotNull(pattern);
        assertTrue(pattern.matcher("abc123_def").matches());
        assertFalse(pattern.matcher("123").matches());
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURLDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String jsonString = "\"invalidUrl\"";
        mapper.readerForUpdating(null).readValueAs(URL.class, deserializer);
    }

    @Test
    public void testLocaleDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "\"en_US\"";
        Locale locale = mapper.readerForUpdating(null).readValueAs(Locale.class, deserializer);
        assertNotNull(locale);
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testLocaleWithOptionalLanguageAndRegion() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "\"_\"";
        Locale locale = mapper.readerForUpdating(null).readValueAs(Locale.class, deserializer);
        assertNotNull(locale);
        assertEquals(Locale.getDefault(), locale);
    }

    @Test
    public void testLocaleWithLanguageAndRegion() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "\"fr_CA\"";
        Locale locale = mapper.readerForUpdating(null).readValueAs(Locale.class, deserializer);
        assertNotNull(locale);
        assertEquals(Locale.CANADA, locale);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidLocaleDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "\"en_US_InvalidRegion\"";
        mapper.readerForUpdating(null).readValueAs(Locale.class, deserializer);
    }

    @Test
    public void testCharsetDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String jsonString = "\"UTF-8\"";
        Charset charset = mapper.readerForUpdating(null).readValueAs(Charset.class, deserializer);
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharsetDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String jsonString = "\"UTF-16\" invalid\"";
        mapper.readerForUpdating(null).readValueAs(Charset.class, deserializer);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String jsonString = "\"America/New_York\"";
        TimeZone timeZone = mapper.readerForUpdating(null).readValueAs(TimeZone.class, deserializer);
        assertNotNull(timeZone);
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeZoneDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String jsonString = "\"InvalidTimeZone\"";
        mapper.readerForUpdating(null).readValueAs(TimeZone.class, deserializer);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String jsonString = "\"192.168.1.1\"";
        InetAddress inetAddress = mapper.readerForUpdating(null).readValueAs(InetAddress.class, deserializer);
        assertNotNull(inetAddress);
        assertEquals("192.168.1.1", inetAddress.getHostAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInetAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String jsonString = "\"256.256.256.256\"";
        mapper.readerForUpdating(null).readValueAs(InetAddress.class, deserializer);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String jsonString = "\"localhost:8080\"";
        InetSocketAddress inetSocketAddress = mapper.readerForUpdating(null).readValueAs(InetSocketAddress.class, deserializer);
        assertNotNull(inetSocketAddress);
        assertEquals("localhost", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInetSocketAddressDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String jsonString = "\"localhost:256\"";
        mapper.readerForUpdating(null).readValueAs(InetSocketAddress.class, deserializer);
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String jsonString = "\"Hello, World!\"";
        StringBuilder stringBuilder = mapper.readerForUpdating(null).readValueAs(StringBuilder.class, deserializer);
        assertNotNull(stringBuilder);
        assertEquals("Hello, World!", stringBuilder.toString());
    }
}