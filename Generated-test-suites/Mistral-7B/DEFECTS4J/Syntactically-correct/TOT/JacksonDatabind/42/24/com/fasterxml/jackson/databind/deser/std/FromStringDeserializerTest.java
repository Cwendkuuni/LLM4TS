package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializationOfFile() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(File.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"path/to/myFile.txt\"";
        File expected = new File("path/to/myFile.txt");
        File result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testDeserializationOfURL() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"http://example.com\"";
        URL expected = new URL("http://example.com");
        URL result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    @Test
    public void testDeserializationOfURI() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"http://example.com/path/to/myFile.txt\"";
        URI expected = new URI("http://example.com/path/to/myFile.txt");
        URI result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.toASCIIString(), result.toASCIIString());
    }

    @Test
    public void testDeserializationOfClass() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"java.lang.String\"";
        Class<?> expected = String.class;
        Class<?> result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfJavaType() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(JavaType.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"com.fasterxml.jackson.databind.JsonNode\"";
        JavaType expected = mapper.getTypeFactory().constructType(JsonNode.class);
        JavaType result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfCurrency() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Currency.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"USD\"";
        Currency expected = Currency.getInstance("USD");
        Currency result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfPattern() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Pattern.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"\\w+@\\w+\\.\\w+\"";
        Pattern expected = Pattern.compile("\\w+@\\w+\\.\\w+");
        Pattern result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfLocale() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"en_US\"";
        Locale expected = Locale.forLanguageTag("en_US");
        Locale result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfCharset() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Charset.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"UTF-8\"";
        Charset expected = Charset.forName("UTF-8");
        Charset result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfTimeZone() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(TimeZone.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"UTC\"";
        TimeZone expected = TimeZone.getTimeZone("UTC");
        TimeZone result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializationOfInetAddress() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"192.168.1.1\"";
        InetAddress expected = InetAddress.getByName("192.168.1.1");
        InetAddress result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.getHostAddress(), result.getHostAddress());
    }

    @Test
    public void testDeserializationOfInetSocketAddress() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(InetSocketAddress.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"192.168.1.1\", 80]";
        InetSocketAddress expected = new InetSocketAddress("192.168.1.1", 80);
        InetSocketAddress result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.getAddress().getHostAddress(), result.getAddress().getHostAddress());
        assertEquals(expected.getPort(), result.getPort());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationExceptionWhenInvalidString() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(String.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"invalidString\"";
        mapper.readValue(json, deserializer.getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationExceptionWhenInvalidClass() throws Exception {
        FromStringDeserializer deserializer = new FromStringDeserializer.Std(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"java.util.Date\"";
        mapper.readValue(json, deserializer.getType());
    }
}