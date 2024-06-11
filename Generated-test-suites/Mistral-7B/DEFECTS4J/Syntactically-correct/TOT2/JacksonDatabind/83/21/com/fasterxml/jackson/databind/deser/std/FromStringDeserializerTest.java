package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeFile() throws JsonProcessingException {
        FromStringDeserializer<File> deserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        File actual = deserializer.deserialize(objectMapper.getFactory().createParser("new File(\"test.txt\")"), objectMapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURL() throws JsonProcessingException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("https://example.com");
        URL actual = deserializer.deserialize(objectMapper.getFactory().createParser("\"https://example.com\""), objectMapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeURI() throws JsonProcessingException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("https://example.com");
        URI actual = deserializer.deserialize(objectMapper.getFactory().createParser("\"https://example.com\""), objectMapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeClass() throws JsonProcessingException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = deserializer.deserialize(objectMapper.getFactory().createParser("new StringBuilder()"), objectMapper.getDeserializationContext());
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidStringForClass() throws JsonProcessingException {
        FromStringDeserializer<StringBuilder> deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"invalidString\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringForURI() throws JsonProcessingException {
        FromStringDeserializer<URI> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        deserializer.deserialize(objectMapper.getFactory().createParser(""), objectMapper.getDeserializationContext());
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedURLString() throws JsonProcessingException {
        FromStringDeserializer<URL> deserializer = FromStringDeserializer.findDeserializer(URL.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"http://example:12345/test\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCurrencyString() throws JsonProcessingException {
        FromStringDeserializer<Currency> deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\" invalidCurrencyCode\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = PatternSyntaxException.class)
    public void testInvalidPatternString() throws JsonProcessingException {
        FromStringDeserializer<Pattern> deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"(.*).json\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleString() throws JsonProcessingException {
        FromStringDeserializer<Locale> deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"en_US_IncorrectFormat\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharsetString() throws JsonProcessingException {
        FromStringDeserializer<Charset> deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"invalidCharset\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeZoneString() throws JsonProcessingException {
        FromStringDeserializer<TimeZone> deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"America/Los_Angeles:InvalidFormat\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInetAddressString() throws JsonProcessingException {
        FromStringDeserializer<InetAddress> deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"invalidIPAddress\""), objectMapper.getDeserializationContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInetSocketAddressString() throws JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        deserializer.deserialize(objectMapper.getFactory().createParser("\"[127.0.0.1:InvalidPort]\""), objectMapper.getDeserializationContext());
    }
}