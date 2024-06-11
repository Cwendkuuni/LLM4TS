package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonTest;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest extends JsonTest {

    @SuppressWarnings("unchecked")
    private static <T> JavaType getTestType() {
        return mapper.getTypeFactory().constructType(FromStringDeserializer.class.getGenericSuperclass());
    }

    @SuppressWarnings("rawtypes")
    private static Class[] getSupportedTypes() {
        return FromStringDeserializer.types();
    }

    @SuppressWarnings("rawtypes")
    private static Std getFromStringDeserializer(Class rawType) {
        return FromStringDeserializer.findDeserializer(rawType);
    }

    @Test
    public void testFileDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(File.class);
        File expected = new File("test.txt");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);

        File result = mapper.readValue(json, getTestType());

        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testUrlDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);

        URL result = mapper.readValue(json, getTestType());

        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    @Test
    public void testUriDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected);

        URI result = mapper.readValue(json, getTestType());

        assertEquals(expected, result);
    }

    @Test
    public void testClassDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(String.class);
        String className = String.class.getName();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(className);

        Class<?> result = mapper.readValue(json, getTestType());

        assertEquals(className, result.getName());
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(JavaType.class);
        ObjectNode node = mapper.createObjectNode();
        node.put("rawClass", String.class.getName());
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        JavaType result = mapper.readValue(json, getTestType());

        JavaType expected = mapper.getTypeFactory().constructType(String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(Currency.class);
        String json = "USD";

        Currency result = mapper.readValue(json, getTestType());

        Currency expected = Currency.getInstance("USD");
        assertEquals(expected.getCurrencyCode(), result.getCurrencyCode());
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(Pattern.class);
        String json = "/[a-zA-Z]+/";

        Pattern result = mapper.readValue(json, getTestType());

        Pattern expected = Pattern.compile("/[a-zA-Z]+/");
        assertEquals(expected.pattern(), result.pattern());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(Locale.class);
        String json = "en_US";

        Locale result = mapper.readValue(json, getTestType());

        Locale expected = Locale.forLanguageTag("en_US");
        assertEquals(expected, result);

        json = "en_US_POSIX";
        result = mapper.readValue(json, getTestType());

        expected = new Locale("en", "US", "");
        assertEquals(expected, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(Charset.class);
        String json = "UTF-8";

        Charset result = mapper.readValue(json, getTestType());

        Charset expected = Charset.forName("UTF-8");
        assertEquals(expected, result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(TimeZone.class);
        String json = "America/Los_Angeles";

        TimeZone result = mapper.readValue(json, getTestType());

        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");
        assertEquals(expected.getID(), result.getID());
    }

    @Test
    public void testInetAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(InetAddress.class);
        String json = "192.168.1.1";

        InetAddress result = mapper.readValue(json, getTestType());

        InetAddress expected = InetAddress.getByName("192.168.1.1");
        assertEquals(expected.getHostAddress(), result.getHostAddress());
    }

    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(InetSocketAddress.class);
        String json1 = "[::1]:8000";
        String json2 = "192.168.1.1:80";

        InetSocketAddress result1 = mapper.readValue(json1, getTestType());
        InetSocketAddress result2 = mapper.readValue(json2, getTestType());

        InetSocketAddress expected1 = new InetSocketAddress(InetAddress.getByName("::1"), 8000);
        InetSocketAddress expected2 = new InetSocketAddress("192.168.1.1", 80);

        assertEquals(expected1.getAddress().getHostAddress(), result1.getAddress().getHostAddress());
        assertEquals(expected1.getPort(), result1.getPort());
        assertEquals(expected2.getAddress().getHostAddress(), result2.getAddress().getHostAddress());
        assertEquals(expected2.getPort(), result2.getPort());
    }

    @Test
    public void testStringBuilderDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(StringBuilder.class);
        String json = "Hello, World!";

        StringBuilder result = mapper.readValue(json, getTestType());

        StringBuilder expected = new StringBuilder("Hello, World!");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDeserializationOfUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(Integer.class);
        String json = "\"Hello, World!\"";

        assertThrows(JsonMappingException.class, () -> mapper.readValue(json, getTestType()));
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(URI.class);

        URI result = mapper.readValue(mapper.writeValueAsString(""), getTestType());

        assertNotNull(result);
        assertNull(result.getScheme());
        assertNull(result.getHost());
        assertNull(result.getPort());
        assertNull(result.getPath());
        assertNull(result.getQuery());
        assertNull(result.getRef());
    }

    @Test
    public void testEmbeddedObjectDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(Locale.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("language", "en");
        node.put("country", "US");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        Locale result = mapper.readValue(json, getTestType());

        Locale expected = new Locale("en", "US");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationFromArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Std deserializer = getFromStringDeserializer(URI[].class);

        String json = "[\"http://example.com\", \"https://example.com\"]";

        URI[] result = mapper.readValue(json, getTestType());

        URI[] expected = new URI[]{new URI("http://example.com"), new URI("https://example.com")};
        assertArrayEquals(expected, result);
    }
}