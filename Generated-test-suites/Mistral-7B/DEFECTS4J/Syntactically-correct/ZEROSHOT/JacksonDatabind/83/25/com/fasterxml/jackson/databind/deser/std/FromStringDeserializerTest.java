package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        String fileString = "path/to/file.txt";
        File file = fileDeserializer.deserialize(objectMapper.getFactory().createParser(fileString), objectMapper.getDeserializationContext());
        assertEquals(new File("path/to/file.txt"), file);
    }

    @Test
    public void testURLDeserialization() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        String urlString = "http://example.com";
        URL url = urlDeserializer.deserialize(objectMapper.getFactory().createParser(urlString), objectMapper.getDeserializationContext());
        assertEquals(new URL("http://example.com"), url);
    }

    @Test
    public void testURIDeserialization() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        String uriString = "http://example.com:8080/path/to";
        URI uri = uriDeserializer.deserialize(objectMapper.getFactory().createParser(uriString), objectMapper.getDeserializationContext());
        assertEquals(new URI("http://example.com:8080/path/to"), uri);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        String stringBuilderString = "new StringBuilder()";
        StringBuilder stringBuilder = stringBuilderDeserializer.deserialize(objectMapper.getFactory().createParser(stringBuilderString), objectMapper.getDeserializationContext());
        assertEquals(new StringBuilder(), stringBuilder);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String currencyString = "USD";
        Currency currency = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(currencyString), objectMapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String patternString = "\\d{3}-\\d{2}-\\d{3}";
        Pattern pattern = patternDeserializer.deserialize(objectMapper.getFactory().createParser(patternString), objectMapper.getDeserializationContext());
        assertEquals(Pattern.compile(patternString), pattern);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String localeString = "en_US";
        Locale locale = localeDeserializer.deserialize(objectMapper.getFactory().createParser(localeString), objectMapper.getDeserializationContext());
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String charsetString = "UTF-8";
        Charset charset = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(charsetString), objectMapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String timeZoneString = "UTC";
        TimeZone timeZone = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(timeZoneString), objectMapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("UTC"), timeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String inetAddressString = "192.168.1.1";
        InetAddress inetAddress = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetAddressString), objectMapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), inetAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String inetSocketAddressString = "[255.255.255.255]:8080";
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetSocketAddressString), objectMapper.getDeserializationContext());
        assertEquals(new InetSocketAddress("255.255.255.255", 8080), inetSocketAddress);
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String emptyString = "";
        stringDeserializer.deserialize(objectMapper.getFactory().createParser(emptyString), objectMapper.getDeserializationContext());
    }

    @Test
    public void testNullDeserialization() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String nullString = null;
        String result = (String) stringDeserializer.deserialize(objectMapper.getFactory().createParser(nullString), objectMapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testUnexpectedTokenDeserialization() throws IOException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String jsonString = "{\"name\":\"John\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);
        jsonParser.nextToken(); // skip START_OBJECT
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> stringDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext()));
        assertEquals("Cannot deserialize instance of \"java.lang.String\" out of START_OBJECT token", exception.getMessage());
    }
}