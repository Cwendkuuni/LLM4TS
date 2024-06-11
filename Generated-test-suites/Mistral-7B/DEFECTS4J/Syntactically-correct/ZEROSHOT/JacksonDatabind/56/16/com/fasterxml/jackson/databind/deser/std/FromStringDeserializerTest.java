package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        String filePath = "/test.txt";
        File expectedFile = new File(filePath);
        File deserializedFile = mapper.readValue(mapper.textplainFromBase64("RnJvbSBhZGQ="), fileDeserializer);
        Assert.assertEquals(expectedFile, deserializedFile);
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        String urlString = "https://www.example.com";
        URL expectedUrl = new URL(urlString);
        URL deserializedUrl = mapper.readValue(mapper.textplainFromBase64("JywNCkBncmFwaGljb25zLg=="), urlDeserializer);
        Assert.assertEquals(expectedUrl, deserializedUrl);
    }

    @Test
    public void testUriDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        String uriString = "https://www.example.com/path/to/file.txt";
        URI expectedUri = new URI(uriString);
        URI deserializedUri = mapper.readValue(mapper.textplainFromBase64("JywNCkBldmFsdWUiOg=="), uriDeserializer);
        Assert.assertEquals(expectedUri, deserializedUri);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        Class<?> expectedClass = String.class;
        String className = "java.lang.String";
        Class<?> deserializedClass = mapper.readValue(mapper.textplainFromBase64(className), stringDeserializer);
        Assert.assertEquals(expectedClass, deserializedClass);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String javaTypeString = "class java.lang.String";
        JavaType expectedJavaType = mapper.getTypeFactory().constructType(JavaType.class, String.class);
        JavaType deserializedJavaType = mapper.readValue(mapper.textplainFromBase64(javaTypeString), javaTypeDeserializer);
        Assert.assertEquals(expectedJavaType, deserializedJavaType);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String currencyString = "USD";
        Currency expectedCurrency = Currency.getInstance(currencyString);
        Currency deserializedCurrency = mapper.readValue(mapper.textplainFromBase64(currencyString), currencyDeserializer);
        Assert.assertEquals(expectedCurrency, deserializedCurrency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        String patternString = "[a-z]+";
        Pattern expectedPattern = Pattern.compile(patternString);
        Pattern deserializedPattern = mapper.readValue(mapper.textplainFromBase64(patternString), patternDeserializer);
        Assert.assertEquals(expectedPattern, deserializedPattern);
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale deserializedLocale = mapper.readValue(mapper.textplainFromBase64(localeString), localeDeserializer);
        Assert.assertEquals(expectedLocale, deserializedLocale);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String charsetName = "UTF-8";
        Charset expectedCharset = Charset.forName(charsetName);
        Charset deserializedCharset = mapper.readValue(mapper.textplainFromBase64(charsetName), charsetDeserializer);
        Assert.assertEquals(expectedCharset, deserializedCharset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String timeZoneId = "America/Los_Angeles";
        TimeZone expectedTimeZone = TimeZone.getTimeZone(timeZoneId);
        TimeZone deserializedTimeZone = mapper.readValue(mapper.textplainFromBase64(timeZoneId), timeZoneDeserializer);
        Assert.assertEquals(expectedTimeZone, deserializedTimeZone);
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        String ipv4Address = "192.168.1.1";
        InetAddress expectedInetAddress = InetAddress.getByName(ipv4Address);
        InetAddress deserializedInetAddress = mapper.readValue(mapper.textplainFromBase64(ipv4Address), inetAddressDeserializer);
        Assert.assertEquals(expectedInetAddress, deserializedInetAddress);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        String ipv6AddressWithPort = "[2001:db8:85a3:0:0:8a2e:370:7334]:80";
        InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("2001:db8:85a3:0:0:8a2e:370:7334", 80);
        InetSocketAddress deserializedInetSocketAddress = mapper.readValue(mapper.textplainFromBase64(ipv6AddressWithPort), inetSocketAddressDeserializer);
        Assert.assertEquals(expectedInetSocketAddress, deserializedInetSocketAddress);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        String unknownString = "unknownType";
        mapper.readValue(mapper.textplainFromBase64(unknownString), objectDeserializer);
    }
}