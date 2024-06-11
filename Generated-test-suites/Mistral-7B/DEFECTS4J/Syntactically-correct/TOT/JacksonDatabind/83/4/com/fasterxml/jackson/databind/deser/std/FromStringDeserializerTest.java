package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        JsonNode node = new JsonFactory().createParser("\"test.txt\"").readValueAsTree();
        File file = fileDeserializer.deserialize(node, new TestContext());
        assertNotNull(file);
        assertTrue(file.exists());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        JsonNode node = new JsonFactory().createParser("\"http://www.example.com\"").readValueAsTree();
        URL url = urlDeserializer.deserialize(node, new TestContext());
        assertNotNull(url);
        assertEquals("http://www.example.com", url.toExternalForm());
    }

    @Test
    public void testUriDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        JsonNode node = new JsonFactory().createParser("\"http://www.example.com/path/to/file\"").readValueAsTree();
        URI uri = uriDeserializer.deserialize(node, new TestContext());
        assertNotNull(uri);
        assertEquals("http://www.example.com/path/to/file", uri.toURI().toURL().toExternalForm());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
        JsonNode node = new JsonFactory().createParser("\"new StringBuilder()\"").readValueAsTree();
        StringBuilder stringBuilder = stringBuilderDeserializer.deserialize(node, new TestContext());
        assertNotNull(stringBuilder);
        assertEquals("", stringBuilder.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Object> objectDeserializer = new FromStringDeserializer<Object>(Object.class);
        JsonNode node = new JsonFactory().createParser("\"new MyUnknownClass()\"").readValueAsTree();
        Object obj = objectDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized an object.");
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedUrlDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        JsonNode node = new JsonFactory().createParser("\"malformed:/url/part\"").readValueAsTree();
        urlDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized a URL.");
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
        JsonNode node = new JsonFactory().createParser("\"EUR\"").readValueAsTree();
        Currency currency = currencyDeserializer.deserialize(node, new TestContext());
        assertNotNull(currency);
        assertEquals(Currency.getInstance("EUR"), currency);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        JsonNode node = new JsonFactory().createParser("\"abcd\"").readValueAsTree();
        localeDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized a Locale.");
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        JsonNode node = new JsonFactory().createParser("\"en_US\"").readValueAsTree();
        Locale locale = localeDeserializer.deserialize(node, new TestContext());
        assertNotNull(locale);
        assertEquals(Locale.US, locale);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        JsonNode node = new JsonFactory().createParser("\"Europe/Paris\"").readValueAsTree();
        TimeZone timeZone = timeZoneDeserializer.deserialize(node, new TestContext());
        assertNotNull(timeZone);
        assertEquals("Europe/Paris", timeZone.getID());
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidTimeZoneIdDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
        JsonNode node = new JsonFactory().createParser("\"InvalidTimeZoneId\"").readValueAsTree();
        timeZoneDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized a TimeZone.");
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
        JsonNode node = new JsonFactory().createParser("\"UTF-8\"").readValueAsTree();
        Charset charset = charsetDeserializer.deserialize(node, new TestContext());
        assertNotNull(charset);
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharsetNameDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
        JsonNode node = new JsonFactory().createParser("\"InvalidCharsetName\"").readValueAsTree();
        charsetDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized a Charset.");
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        JsonNode node = new JsonFactory().createParser("\"127.0.0.1\"").readValueAsTree();
        InetAddress inetAddress = inetAddressDeserializer.deserialize(node, new TestContext());
        assertNotNull(inetAddress);
        assertEquals("127.0.0.1", inetAddress.getHostAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        JsonNode node = new JsonFactory().createParser("\"abcd\"").readValueAsTree();
        inetAddressDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized an InetAddress.");
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        JsonNode node = new JsonFactory().createParser("\"[127.0.0.1]:8080\"").readValueAsTree();
        InetSocketAddress inetSocketAddress = inetSocketAddressDeserializer.deserialize(node, new TestContext());
        assertNotNull(inetSocketAddress);
        assertEquals("127.0.0.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        JsonNode node = new JsonFactory().createParser("\"abcd:8080\"").readValueAsTree();
        inetSocketAddressDeserializer.deserialize(node, new TestContext());
        fail("Unexpectedly deserialized an InetSocketAddress.");
    }

    static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper _getConfig() {
            return new ObjectMapper();
        }

        @Override
        public JavaType _handleUnknownType(Class<?> rawType, PropertyName propertyName) throws JsonMappingException {
            throw new JsonMappingException("Unknown type handling not supported.");
        }
    }
}