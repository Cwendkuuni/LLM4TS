package com.fasterxml.jackson.databind.deser.std; // import statements go here

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.test.BaseTest;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest extends BaseTest {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // for _deserializeFromEmptyString()
    }

    // Test for File deserialization
    @Test
    public void testFileDeserialization() throws IOException {
        String json = "{\"className\":\"File\",\"path\":\"test.txt\"}";
        File deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("test.txt", deserializedObject.getAbsolutePath());
    }

    // Test for URL deserialization
    @Test
    public void testURLDeserialization() throws IOException {
        String json = "{\"className\":\"URL\",\"value\":\"http://example.com\"}";
        URL deserializedObject = mapser.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("http://example.com", deserializedObject.toString());
    }

    // Test for URI deserialization
    @Test
    public void testURIDeserialization() throws IOException {
        String json = "{\"className\":\"URI\",\"value\":\"http://example.com/path/to/resource\"}";
        URI deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("http://example.com/path/to/resource", deserializedObject.toString());
    }

    // Test for Class deserialization
    @Test
    public void testClassDeserialization() throws IOException {
        String json = "{\"className\":\"Class\",\"name\":\"java.util.Date\"}";
        Class<?> deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(java.util.Date.class, deserializedObject);
    }

    // Test for JavaType deserialization
    @Test
    public void testJavaTypeDeserialization() throws IOException {
        String json = "{\"className\":\"JavaType\",\"typeName\":\"com.fasterxml.jackson.databind.JsonNode\"}";
        JavaType deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(com.fasterxml.jackson.databind.JsonNode.class, deserializedObject.getRawClass());
    }

    // Test for Currency deserialization
    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "{\"className\":\"Currency\",\"currencyCode\":\"EUR\"}";
        Currency deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(Currency.EUR, deserializedObject);
    }

    // Test for Pattern deserialization
    @Test
    public void testPatternDeserialization() throws IOException {
        String json = "{\"className\":\"Pattern\",\"pattern\":\"\\d+\\\\.\\d+\\\\d+\"}";
        Pattern deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals(Pattern.compile("\\d+\\\\.\\d+\\\\d+"), deserializedObject);
    }

    // Test for Locale deserialization
    @Test
    public void testLocaleDeserialization() throws IOException {
        String json1 = "{\"className\":\"Locale\",\"localeString\":\"en_US\"}";
        Locale deserializedObject1 = mapper.readValue(json1, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json1), null);
        assertEquals(Locale.US, deserializedObject1);

        String json2 = "{\"className\":\"Locale\",\"localeString\":\"fr_FR.UTF-8\"}";
        Locale deserializedObject2 = mapper.readValue(json2, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json2), null);
        assertEquals(Locale.FRANCE, deserializedObject2);
    }

    // Test for Charset deserialization
    @Test
    public void testCharsetDeserialization() throws IOException {
        String json1 = "{\"className\":\"Charset\",\"charsetName\":\"US-ASCII\"}";
        Charset deserializedObject1 = mapper.readValue(json1, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json1), null);
        assertEquals(Charset.forName("US-ASCII"), deserializedObject1);

        String json2 = "{\"className\":\"Charset\",\"charsetName\":\"UTF-8\"}";
        Charset deserializedObject2 = mapper.readValue(json2, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json2), null);
        assertEquals(Charset.forName("UTF-8"), deserializedObject2);
    }

    // Test for TimeZone deserialization
    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json1 = "{\"className\":\"TimeZone\",\"timeZoneID\":\"US/Eastern\"}";
        TimeZone deserializedObject1 = mapper.readValue(json1, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json1), null);
        assertEquals("America/New_York", deserializedObject1.getID());

        String json2 = "{\"className\":\"TimeZone\",\"timeZoneID\":\"Europe/London\"}";
        TimeZone deserializedObject2 = mapper.readValue(json2, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json2), null);
        assertEquals("Europe/London", deserializedObject2.getID());
    }

    // Test for InetAddress deserialization
    @Test
    public void testInetAddressDeserialization() throws IOException {
        String ipv4Json = "{\"className\":\"InetAddress\",\"address\":\"192.168.1.1\"}";
        InetAddress ipv4DeserializedObject = mapper.readValue(ipv4Json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(ipv4Json), null);
        assertEquals("192.168.1.1", ipv4DeserializedObject.getHostAddress());

        String ipv6Json = "{\"className\":\"InetAddress\",\"address\":\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\"}";
        InetAddress ipv6DeserializedObject = mapper.readValue(ipv6Json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(ipv6Json), null);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", ipv6DeserializedObject.getHostAddress());
    }

    // Test for InetSocketAddress deserialization
    @Test
    public void testInetSocketAddressDeserialization() throws IOException {
        String json1 = "{\"className\":\"InetSocketAddress\",\"address\":\"192.168.1.1\",\"port\":\"80\"}";
        InetSocketAddress deserializedObject1 = mapper.readValue(json1, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json1), null);
        assertEquals("192.168.1.1", deserializedObject1.getAddress().getHostAddress());
        assertEquals(80, deserializedObject1.getPort());

        String json2 = "{\"className\":\"InetSocketAddress\",\"address\":\"[2001:0db8:85a3:0000:0000:8a2e:0370:7334]\",\"port\":\"80\"}";
        InetSocketAddress deserializedObject2 = mapper.readValue(json2, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json2), null);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", deserializedObject2.getAddress().getHostAddress());
        assertEquals(80, deserializedObject2.getPort());
    }

    // Test for StringBuilder deserialization
    @Test
    public void testStringBuilderDeserialization() throws IOException {
        String json = "{\"className\":\"StringBuilder\",\"value\":\"Test string builder\"}";
        StringBuilder deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertEquals("Test string builder", deserializedObject.toString());
    }

    // Test for empty String deserialization
    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        Object deserializedObject = mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
        assertNull(deserializedObject);
    }

    // Test for unexpected JSON token
    @Test(expected = JsonMappingException.class)
    public void testUnexpectedJsonToken() throws IOException {
        String json = "[1, 2, 3]";
        mapper.readValue(json, FromStringDeserializer.class).deserialize(mapper.getFactory().createParser(json), null);
    }
}