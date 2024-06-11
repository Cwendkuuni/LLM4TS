package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws IOException, URISyntaxException {
        String json = "\"/path/to/file.txt\"";
        File file = new File("testFile.txt").getAbsoluteFile();
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer.Std(File.class);

        File deserializedFile = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(file, deserializedFile);
        assertEquals(file.getAbsolutePath(), deserializedFile.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        String json = "\"http://example.com/path?query=param\"";
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer.Std(URL.class);

        URL deserializedUrl = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(new URI("http", "example.com", "/path", null, "query=param", null).toURL(), deserializedUrl);
    }

    @Test
    public void testUriDeserialization() throws IOException, URISyntaxException {
        String json = "\"http://example.com/path?query=param\"";
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer.Std(URI.class);

        URI deserializedUri = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        URI expectedUri = new URI("http", "example.com", "/path", null, "query=param", null);
        assertEquals(expectedUri, deserializedUri);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        String json = "\"java.lang.String\"";
        FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer.Std(Class.class);

        Class<?> deserializedClass = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(String.class, deserializedClass);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "\"com.example.MyType\"";
        JavaType javaType = objectMapper.getTypeFactory().constructType(new TextNode(json).asText());

        FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer.Std(JavaType.class);

        JavaType deserializedJavaType = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(javaType, deserializedJavaType);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "\"EUR\"";
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer.Std(Currency.class);

        Currency deserializedCurrency = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(Currency.getInstance("EUR"), deserializedCurrency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "/[a-z]+/i";
        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer.Std(Pattern.class);

        Pattern deserializedPattern = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        Pattern expectedPattern = Pattern.compile(json);
        assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "\"en_US\"";
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer.Std(Locale.class);

        Locale deserializedLocale = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(Locale.forLanguageTag("en_US"), deserializedLocale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        String json = "\"UTF-8\"";
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer.Std(Charset.class);

        Charset deserializedCharset = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(Charset.forName(json), deserializedCharset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "\"UTC\"";
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer.Std(TimeZone.class);

        TimeZone deserializedTimeZone = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(TimeZone.getTimeZone("UTC"), deserializedTimeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        String json = "\"127.0.0.1\"";
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer.Std(InetAddress.class);

        InetAddress deserializedInetAddress = objectMapper.readerForUpdating(null)
                .with(DeserializationFeature.FAIL_ON_EMPTY_BEANS)
                .readValue(new TextNode(json));

        assertEquals(InetAddress.getByName("127.0.0.1"), deserializedInetAddress);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        String json = "\"InvalidType\"";
        FromStringDeserializer<Object> invalidDeserializer = new FromStringDeserializer.Std(Object.class);

        assertThrows(JsonProcessingException.class, () -> {
            invalidDeserializer.deserialize(objectMapper.getFactory().createParser(new TextNode(json)), objectMapper.getDeserializationContext());
        });
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<String> emptyStringDeserializer = new FromStringDeserializer.Std(String.class);

        assertNull(emptyStringDeserializer.deserialize(objectMapper.getFactory().createParser(new TextNode("")), objectMapper.getDeserializationContext()));
    }

    @Test
    public void testNullDeserialization() throws IOException {
        String json = "null";
        FromStringDeserializer<String> nullDeserializer = new FromStringDeserializer.Std(String.class);

        assertNull(nullDeserializer.deserialize(objectMapper.getFactory().createParser(new TextNode(json)), objectMapper.getDeserializationContext()));
    }
}