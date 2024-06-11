package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class FromStringDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFileDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertEquals(new File("test.txt"), deserializer.deserialize(mapper.getFactory().createParser("new File(\"test.txt\")"), mapper.getContext()));
    }

    @Test
    public void testUrlDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertEquals(new URL("http://test.com"), deserializer.deserialize(mapper.getFactory().createParser("http://test.com"), mapper.getContext()));
    }

    @Test
    public void testUriDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        assertEquals(new URI("http://test.com"), deserializer.deserialize(mapper.getFactory().createParser("http://test.com"), mapper.getContext()));
    }

    @Test
    public void testClassDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNotNull(deserializer.deserialize(mapper.getFactory().createParser("java.lang.String"), mapper.getContext()));
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer[].class);
        JavaType javaType = mapper.getTypeFactory().constructType(Integer[].class);
        assertNotNull(deserializer.deserialize(mapper.getFactory().createParser("java.util.Arrays.asList(1, 2, 3)"), mapper.getContext()));
        assertEquals(javaType.getClass(), deserializer.deserialize(mapper.getFactory().createParser("java.util.ArrayList<java.lang.Integer>.class"), mapper.getContext()).getType().getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        assertEquals(Currency.getInstance("USD"), deserializer.deserialize(mapper.getFactory().createParser("USD"), mapper.getContext()));
    }

    @Test
    public void testPatternDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        assertEquals(Pattern.compile("[a-z]+"), deserializer.deserialize(mapper.getFactory().createParser("\\w+"), mapper.getContext()));
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertEquals(Locale.US, deserializer.deserialize(mapper.getFactory().createParser("en_US"), mapper.getContext()));
        assertEquals(Locale.FRANCE, deserializer.deserialize(mapper.getFactory().createParser("fr_FR"), mapper.getContext()));
        assertEquals(Locale.JAPAN, deserializer.deserialize(mapper.getFactory().createParser("ja_JP"), mapper.getContext()));
    }

    @Test
    public void testCharsetDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertEquals(Charset.forName("UTF-8"), deserializer.deserialize(mapper.getFactory().createParser("UTF-8"), mapper.getContext()));
    }

    @Test
    public void testTimeZoneDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertEquals(TimeZone.getTimeZone("UTC"), deserializer.deserialize(mapper.getFactory().createParser("UTC"), mapper.getContext()));
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), deserializer.deserialize(mapper.getFactory().createParser("Europe/Paris"), mapper.getContext()));
    }

    @Test
    public void testInetAddressDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertEquals(InetAddress.getByName("127.0.0.1"), deserializer.deserialize(mapper.getFactory().createParser("127.0.0.1"), mapper.getContext()));
        assertEquals(InetAddress.getByName("255.255.255.255"), deserializer.deserialize(mapper.getFactory().createParser("255.255.255.255"), mapper.getContext()));
    }

    @Test
    public void testInetSocketAddressDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertEquals(new InetSocketAddress("localhost", 8080), deserializer.deserialize(mapper.getFactory().createParser("localhost:8080"), mapper.getContext()));
        assertEquals(new InetSocketAddress("192.168.1.1", 8080), deserializer.deserialize(mapper.getFactory().createParser("192.168.1.1:8080"), mapper.getContext()));
        assertEquals(new InetSocketAddress(new InetSocketAddress("2001:0db8:85a3:0000:0000:8a2e:0370:7334"), 80), deserializer.deserialize(mapper.getFactory().createParser("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80"), mapper.getContext()));
    }

    @Test
    public void testStringBuilderDeserialization() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        assertEquals(new StringBuilder("test"), deserializer.deserialize(mapper.getFactory().createParser("test"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testUnexpectedToken() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        mapper.getFactory().createParser("[1, 2, 3]").nextToken(); // consume [ and [
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("[1, 2, 3]"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedUrl() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("jabber://test.com"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testNullValue() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("null"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testEmptyString() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser(""), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedLocale() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("en_US_XXX"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedCharset() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("UTF-8X"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedTimeZone() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("Asia/XXXX"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedInetAddress() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("256.0.0.1"), mapper.getContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testMalformedInetSocketAddress() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        assertThrows(JsonMappingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("localhost:8080:80"), mapper.getContext()));
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidFormatException() throws Exception {
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        assertThrows(JsonProcessingException.class, () -> deserializer.deserialize(mapper.getFactory().createParser("invalid"), mapper.getContext()));
    }
}