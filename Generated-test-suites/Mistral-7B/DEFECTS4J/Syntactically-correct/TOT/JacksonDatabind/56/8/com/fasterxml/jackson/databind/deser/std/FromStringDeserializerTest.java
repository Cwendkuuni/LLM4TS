package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFileDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"C:\\test.txt\"");
        Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = fileDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("C:\\test.txt", file.getPath());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"http://www.example.com\"");
        Std.StdURL urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = urlDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"http://www.example.com/path?query=value#fragment\"");
        Std.StdURI uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = uriDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("http", uri.getScheme());
        assertEquals("/path", uri.getPath());
        assertEquals("query=value", uri.getQuery());
        assertEquals("fragment", uri.getFragment());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"java.lang.String\"");
        Std.StdClass classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String string = classDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("java.lang.String", string.getClass().getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JsonNode.class\"");
        Std.StdJavaType javaTypeDeserializer = FromStringDeserializer.findDeserializer(JsonNode.class);
        JavaType javaType = javaTypeDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals(JsonNode.class, javaType.getType());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"EUR\"");
        Std.StdCurrency currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = currencyDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("EUR"), currency);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"/^\\d{3}\\s\\d{2}\\s\\d{4}$/\"");
        Std.StdPattern patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern pattern = patternDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertTrue(pattern.matches("123 456 7890"));
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"en_US\"");
        Std.StdLocale localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = localeDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());

        p = mapper.getFactory().createParser("\"en_US_POSIX\"");
        localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        locale = localeDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("en", locale.getLanguage());
        assertEquals("POSIX", locale.getVariants()[0]);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"UTF-8\"");
        Std.StdCharset charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset charset = charsetDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"America/New_York\"");
        Std.StdTimeZone timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = timeZoneDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("America/New_York", timeZone.getID());
        assertEquals("America/New_York", timeZone.getDisplayName());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"192.168.1.1\"");
        Std.StdInetAddress inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("192.168.1.1", inetAddress.getHostAddress());

        p = mapper.getFactory().createParser("\"[2001:db8:0:12f:0:0:0:1]\"");
        inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        inetAddress = inetAddressDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("2001:db8:0:12f:0:0:0:1", inetAddress.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"[2001:db8:0:12f:0:0:0:1]:80\"");
        Std.StdInetSocketAddress inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("2001:db8:0:12f:0:0:0:1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress.getPort());

        p = mapper.getFactory().createParser("\"192.168.1.1:8080\"");
        inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        inetSocketAddress = inetSocketAddressDeserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals("192.168.1.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        p = mapper.getFactory().createParser("\"[255.255.255.255]:8080\"");
        inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        try {
            inetSocketAddress = inetSocketAddressDeserializer.deserialize(p, mapper.getDeserializationContext());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals(null, result);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("null");
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        Object result = deserializer.deserialize(p, mapper.getDeserializationContext());
        assertEquals(null, result);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        JsonParser p = mapper.getFactory().createParser("\"[1,2,3]\"" + System.lineSeparator() + "[\"test\"]");
        Std.StdFile fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        try {
            fileDeserializer.deserialize(p, mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}