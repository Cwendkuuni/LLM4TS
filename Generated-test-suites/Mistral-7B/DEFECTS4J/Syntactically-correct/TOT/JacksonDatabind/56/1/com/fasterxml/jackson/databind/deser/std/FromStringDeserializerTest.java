package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"path/to/file.txt\"");
        Std.StdFile deserializer = (Std.StdFile) FromStringDeserializer.findDeserializer(File.class);
        File result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testUrlDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/\"");
        Std.StdURL deserializer = (Std.StdURL) FromStringDeserializer.findDeserializer(URL.class);
        URL result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new URL("http://example.com/"), result);
    }

    @Test
    public void testUriDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"http://example.com/path/to/resource?query1=value1&query2=value2\"");
        Std.StdURI deserializer = (Std.StdURI) FromStringDeserializer.findDeserializer(URI.class);
        URI result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new URI("http://example.com/path/to/resource?query1=value1&query2=value2"), result);
    }

    @Test
    public void testClassDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"java.lang.String.class\"");
        Std.StdClass deserializer = (Std.StdClass) FromStringDeserializer.findDeserializer(String.class);
        String result = (String) deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals("java.lang.String.class", result);
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JsonNode.class\"");
        Std.StdJavaType deserializer = (Std.StdJavaType) FromStringDeserializer.findDeserializer(JsonNode.class);
        JavaType result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(JsonNode.class, result.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"USD\"");
        Std.StdCurrency deserializer = (Std.StdCurrency) FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"/^[a-z]+$/i\"");
        Std.StdPattern deserializer = (Std.StdPattern) FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Pattern.compile("/^[a-z]+$/i"), result);
    }

    @Test
    public void testLocaleDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"en_US\"");
        Std.StdLocale deserializer = (Std.StdLocale) FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_US_POSIX\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.US, result);

        parser = mapper.getFactory().createParser("\"en_GB\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.UK, result);

        parser = mapper.getFactory().createParser("\"fr_FR_FR\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Locale.FRANCE, result);
    }

    @Test
    public void testCharsetDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"UTF-8\"");
        Std.StdCharset deserializer = (Std.StdCharset) FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"America/Los_Angeles\"");
        Std.StdTimeZone deserializer = (Std.StdTimeZone) FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(TimeZone.getTimeZone("America/Los_Angeles"), result);
    }

    @Test
    public void testInetAddressDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"192.168.1.1\"");
        Std.StdInetAddress deserializer = (Std.StdInetAddress) FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testInetSocketAddressDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"[255.255.255.255]:80\"");
        Std.StdInetSocketAddress deserializer = (Std.StdInetSocketAddress) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), result);

        parser = mapper.getFactory().createParser("\"192.168.1.1:80\"");
        result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), result);
    }

    @Test
    public void testEmptyStringDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        String result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test
    public void testInvalidDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"invalid_value\"");
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class);
        try {
            deserializer.deserialize(parser, mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}