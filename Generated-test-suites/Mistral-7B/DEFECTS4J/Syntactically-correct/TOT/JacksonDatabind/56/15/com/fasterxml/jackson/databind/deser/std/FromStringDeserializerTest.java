package com.fasterxml.jackson.databind.deser.std.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TestBase;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest extends TestBase {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testFileDeserializer() throws IOException {
        Std fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), fileDeserializer.deserialize(makeParserFor("\"test.txt\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testURLDeserializer() throws IOException {
        Std urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://example.com"), urlDeserializer.deserialize(makeParserFor("\"http://example.com\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testURIDeserializer() throws IOException {
        Std uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://example.com"), uriDeserializer.deserialize(makeParserFor("\"http://example.com\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testClassDeserializer() throws IOException {
        Std classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        assertEquals(String.class, classDeserializer.deserialize(makeParserFor("\"java.lang.String\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testJavaTypeDeserializer() throws IOException {
        Std javaTypeDeserializer = FromStringDeserializer.findDeserializer(String.class.getClassType());
        JavaType javaType = mapper.getTypeFactory().constructType(javaTypeDeserializer.getClass().getCanonicalName());
        assertEquals(javaType, javaTypeDeserializer.deserialize(makeParserFor("\"java.lang.reflect.JavaType<java.lang.String>\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCurrencyDeserializer() throws IOException {
        Std currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.getInstance("USD"), currencyDeserializer.deserialize(makeParserFor("\"USD\""), null));

        try {
            currencyDeserializer.deserialize(makeParserFor("\"JPY\""), null);
            fail("Expected InvalidFormatException");
        } catch (InvalidFormatException e) {
            // Expected
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testPatternDeserializer() throws IOException {
        Std patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertEquals(Pattern.compile("\\d+"), patternDeserializer.deserialize(makeParserFor("\"\\\\d+\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testLocaleDeserializer() throws IOException {
        Std localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.ENGLISH, localeDeserializer.deserialize(makeParserFor("\"en_US\""), null));
        assertEquals(Locale.forLanguageTag("fr_CA"), localeDeserializer.deserialize(makeParserFor("\"fr_CA\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCharsetDeserializer() throws IOException {
        Std charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), charsetDeserializer.deserialize(makeParserFor("\"UTF-8\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testTimeZoneDeserializer() throws IOException {
        Std timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), timeZoneDeserializer.deserialize(makeParserFor("\"UTC\""), null));
        assertEquals(TimeZone.getTimeZone("America/New_York"), timeZoneDeserializer.deserialize(makeParserFor("\"America/New_York\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testInetAddressDeserializer() throws IOException {
        Std inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertEquals(InetAddress.getByName("127.0.0.1"), inetAddressDeserializer.deserialize(makeParserFor("\"127.0.0.1\""), null));
        assertEquals(InetAddress.getByName("255.255.255.255"), inetAddressDeserializer.deserialize(makeParserFor("\"255.255.255.255\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testInetSocketAddressDeserializer() throws IOException {
        Std inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertEquals(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 80), inetSocketAddressDeserializer.deserialize(makeParserFor("\"127.0.0.1:80\""), null));
        assertEquals(new InetSocketAddress(InetAddress.getByName("::1"), 80), inetSocketAddressDeserializer.deserialize(makeParserFor("\"::1:80\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testEmptyStringDeserializer() throws IOException {
        Std emptyStringDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertNull(emptyStringDeserializer.deserialize(makeParserFor("\"\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testEmptyArrayDeserializer() throws IOException {
        Std emptyArrayDeserializer = FromStringDeserializer.findDeserializer(String[].class);
        assertEquals(new String[0], emptyArrayDeserializer.deserialize(makeParserFor("[]"), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testComplexDeserialization() throws IOException {
        Std complexDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.ENGLISH, complexDeserializer.deserialize(makeParserFor("\"en_US_POSIX\""), null));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws IOException {
        Std invalidDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        invalidDeserializer.deserialize(makeParserFor("\"hello\""), null);
    }

    private JsonParser makeParserFor(String jsonString) throws IOException {
        return mapper.getFactory().createParser(jsonString.getBytes("UTF-8"));
    }

}