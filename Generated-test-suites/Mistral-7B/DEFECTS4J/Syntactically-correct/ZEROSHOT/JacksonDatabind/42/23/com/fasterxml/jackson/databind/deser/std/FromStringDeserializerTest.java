package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.java.TypeFactory;

import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFileDeserializer() throws JsonProcessingException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(fileDeserializer);
        File file = fileDeserializer.deserialize(objectMapper.getFactory().createParser("path/to/file.txt"), objectMapper.getDeserializationContext());
        assertNotNull(file);
    }

    @Test
    public void testUrlDeserializer() throws JsonProcessingException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertNotNull(urlDeserializer);
        URL url = urlDeserializer.deserialize(objectMapper.getFactory().createParser("http://example.com"), objectMapper.getDeserializationContext());
        assertNotNull(url);
    }

    @Test
    public void testUriDeserializer() throws JsonProcessingException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertNotNull(uriDeserializer);
        URI uri = uriDeserializer.deserialize(objectMapper.getFactory().createParser("http://example.com/path"), objectMapper.getDeserializationContext());
        assertNotNull(uri);
    }

    @Test
    public void testClassDeserializer() throws JsonProcessingException {
        FromStringDeserializer<String.class> clazzDeserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNotNull(clazzDeserializer);
        String result = (String) clazzDeserializer.deserialize(objectMapper.getFactory().createParser("java.lang.String"), objectMapper.getDeserializationContext());
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testJavaTypeDeserializer() throws JsonProcessingException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        FromStringDeserializer<JavaType> javaTypeDeserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        assertNotNull(javaTypeDeserializer);
        JavaType result = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser("java.lang.String"), objectMapper.getDeserializationContext());
        assertEquals(javaType, result);
    }

    @Test
    public void testCurrencyDeserializer() throws JsonProcessingException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertNotNull(currencyDeserializer);
        Currency currency = currencyDeserializer.deserialize(objectMapper.getFactory().createParser("USD"), objectMapper.getDeserializationContext());
        assertNotNull(currency);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testPatternDeserializer() throws JsonProcessingException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertNotNull(patternDeserializer);
        Pattern pattern = patternDeserializer.deserialize(objectMapper.getFactory().createParser("/[a-z]+/i"), objectMapper.getDeserializationContext());
        assertNotNull(pattern);
        assertTrue(pattern.matcher("example").matches());
    }

    @Test
    public void testLocaleDeserializer() throws JsonProcessingException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertNotNull(localeDeserializer);
        Locale result = localeDeserializer.deserialize(objectMapper.getFactory().createParser("en_US"), objectMapper.getDeserializationContext());
        assertNotNull(result);
        assertEquals("en_US", result.toString());
    }

    @Test
    public void testCharsetDeserializer() throws JsonProcessingException {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertNotNull(charsetDeserializer);
        Charset charset = charsetDeserializer.deserialize(objectMapper.getFactory().createParser("UTF-8"), objectMapper.getDeserializationContext());
        assertNotNull(charset);
        assertEquals("UTF-8", charset.name());
    }

    @Test
    public void testTimeZoneDeserializer() throws JsonProcessingException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertNotNull(timeZoneDeserializer);
        TimeZone timeZone = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser("UTC"), objectMapper.getDeserializationContext());
        assertNotNull(timeZone);
        assertEquals("UTC", timeZone.getID());
    }

    @Test
    public void testInetAddressDeserializer() throws JsonProcessingException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertNotNull(inetAddressDeserializer);
        InetAddress inetAddress = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser("192.168.1.1"), objectMapper.getDeserializationContext());
        assertNotNull(inetAddress);
    }

    @Test
    public void testInetSocketAddressDeserializer() throws JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertNotNull(inetSocketAddressDeserializer);

        InetSocketAddress inetSocketAddress1 = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser("[::1]:80"), objectMapper.getDeserializationContext());
        assertNotNull(inetSocketAddress1);
        assertEquals("[::1]", inetSocketAddress1.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress1.getPort());

        InetSocketAddress inetSocketAddress2 = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser("192.168.1.1:80"), objectMapper.getDeserializationContext());
        assertNotNull(inetSocketAddress2);
        assertEquals("192.168.1.1", inetSocketAddress2.getAddress().getHostAddress());
        assertEquals(80, inetSocketAddress2.getPort());
    }
}