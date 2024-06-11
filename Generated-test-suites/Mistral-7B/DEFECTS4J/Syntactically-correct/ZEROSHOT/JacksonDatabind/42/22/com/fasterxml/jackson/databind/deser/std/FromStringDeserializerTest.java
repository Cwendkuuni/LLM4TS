package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws JsonProcessingException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File expected = new File("test.txt");
        File actual = fileDeserializer.deserialize(objectMapper.getFactory().createParser("new File(\"test.txt\")"), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUrlDeserialization() throws JsonProcessingException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expected = new URL("http://example.com");
        URL actual = urlDeserializer.deserialize(objectMapper.getFactory().createParser("\"http://example.com\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUriDeserialization() throws JsonProcessingException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expected = new URI("http://example.com");
        URI actual = uriDeserializer.deserialize(objectMapper.getFactory().createParser("\"http://example.com\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testClassDeserialization() throws JsonProcessingException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String expected = "Hello, World!";
        String actual = stringDeserializer.deserialize(objectMapper.getFactory().createParser("\"" + expected + "\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJavaTypeDeserialization() throws JsonProcessingException {
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        JavaType expected = objectMapper.getTypeFactory().constructType(String.class);
        JavaType actual = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser("java.lang.String"), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCurrencyDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expected = Currency.getInstance("USD");
        Currency actual = currencyDeserializer.deserialize(objectMapper.getFactory().createParser("\"USD\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPatternDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expected = Pattern.compile("\\d+");
        Pattern actual = patternDeserializer.deserialize(objectMapper.getFactory().createParser("\".*\\\\d+.*\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected.pattern(), actual.pattern());
    }

    @Test
    public void testLocaleDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expected = new Locale("en", "US");
        Locale actual = localeDeserializer.deserialize(objectMapper.getFactory().createParser("\"en-US\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCharsetDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expected = Charset.forName("UTF-8");
        Charset actual = charsetDeserializer.deserialize(objectMapper.getFactory().createParser("\"" + Charset.NAME_UTF_8 + "\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTimeZoneDeserialization() throws JsonProcessingException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone actual = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser("\"" + TimeZone.getTimeZone("UTC").getID() + "\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInetAddressDeserialization() throws JsonProcessingException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expected = InetAddress.getByName("127.0.0.1");
        InetAddress actual = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser("\"127.0.0.1\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expected = new InetSocketAddress("localhost", 8080);
        InetSocketAddress actual = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser("\"localhost:8080\""), objectMapper.getDeserializationContext());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
        Assert.assertEquals(expected.getPort(), actual.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws JsonProcessingException {
        FromStringDeserializer<Integer> integerDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        integerDeserializer.deserialize(objectMapper.getFactory().createParser("\"Hello, World!\""), objectMapper.getDeserializationContext());
    }
}