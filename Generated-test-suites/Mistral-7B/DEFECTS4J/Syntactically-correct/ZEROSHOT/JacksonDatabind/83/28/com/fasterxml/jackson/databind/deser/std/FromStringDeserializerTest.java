package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.TestUtil;
import com.fasterxml.jackson.databind.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.testng.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        ObjectMapper mapper = new ObjectMapper();
        String fileString = "path/to/file.txt";
        File expectedFile = new File(fileString);
        File actualFile = mapper.readValue(mapper.textParser().readValueAsString(fileString), fileDeserializer);
        assertEquals(actualFile, expectedFile);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        String urlString = "http://www.example.com";
        URL expectedUrl = new URL(urlString);
        URL actualUrl = mapper.readValue(mapper.textParser().readValueAsString(urlString), urlDeserializer);
        assertEquals(actualUrl, expectedUrl);
    }

    @Test
    public void testUriDeserialization() throws IOException {
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        String uriString = "http://www.example.com/path/to/resource";
        URI expectedUri = new URI(uriString);
        URI actualUri = mapper.readValue(mapper.textParser().readValueAsString(uriString), uriDeserializer);
        assertEquals(actualUri, expectedUri);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
        ObjectMapper mapper = new ObjectMapper();
        String stringBuilderString = "new StringBuilder()";
        StringBuilder expectedStringBuilder = new StringBuilder();
        StringBuilder actualStringBuilder = mapper.readValue(mapper.textParser().readValueAsString(stringBuilderString), stringBuilderDeserializer);
        assertEquals(actualStringBuilder, expectedStringBuilder);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        String currencyString = "USD";
        Currency expectedCurrency = Currency.getInstance(Locale.US);
        Currency actualCurrency = mapper.readValue(mapper.textParser().readValueAsString(currencyString), currencyDeserializer);
        assertEquals(actualCurrency, expectedCurrency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        String patternString = "\\w+";
        Pattern expectedPattern = Pattern.compile(patternString);
        Pattern actualPattern = mapper.readValue(mapper.textParser().readValueAsString(patternString), patternDeserializer);
        assertEquals(actualPattern.pattern(), expectedPattern.pattern());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = mapper.readValue(mapper.textParser().readValueAsString(localeString), localeDeserializer);
        assertEquals(actualLocale, expectedLocale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        String charsetString = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetString);
        Charset actualCharset = mapper.readValue(mapper.textParser().readValueAsString(charsetString), charsetDeserializer);
        assertEquals(actualCharset, expectedCharset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        String timeZoneString = "America/New_York";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneString);
        TimeZone actualTimeZone = mapper.readValue(mapper.textParser().readValueAsString(timeZoneString), timeZoneDeserializer);
        assertEquals(actualTimeZone, expectedTimeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String inetAddressString = "192.168.1.1";
        InetAddress expectedInetAddress = InetAddress.getByName(inetAddressString);
        InetAddress actualInetAddress = mapper.readValue(mapper.textParser().readValueAsString(inetAddressString), inetAddressDeserializer);
        assertEquals(actualInetAddress, expectedInetAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String inetSocketAddressString = "[255.255.255.255]:8080";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("255.255.255.255", 8080);
        InetSocketAddress actualInetSocketAddress = mapper.readValue(mapper.textParser().readValueAsString(inetSocketAddressString), inetSocketAddressDeserializer);
        assertEquals(actualInetSocketAddress, expectedInetSocketAddress);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        FromStringDeserializer<Object> emptyStringDeserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        String emptyString = "";
        Object actualEmptyString = mapper.readValue(mapper.textParser().readValueAsString(emptyString), emptyStringDeserializer);
        assertNull(actualEmptyString);
    }
}