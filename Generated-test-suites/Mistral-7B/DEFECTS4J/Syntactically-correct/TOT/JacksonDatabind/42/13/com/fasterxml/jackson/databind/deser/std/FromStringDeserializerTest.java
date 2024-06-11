package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        String json = "{\"className\":\"java.io.File\",\"value\":\"test.txt\"}";
        File expected = new File("test.txt");
        File result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testURLDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String json = "{\"className\":\"java.net.URL\",\"value\":\"http://example.com\"}";
        URL expected = new URL("http://example.com");
        URL result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.toExternalForm(), result.toExternalForm());
    }

    @Test
    public void testURIDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String json = "{\"className\":\"java.net.URI\",\"value\":\"http://example.com/path/to/file\"}";
        URI expected = new URI("http://example.com/path/to/file");
        URI result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected.toASCIIString(), result.toASCIIString());
    }

    @Test
    public void testClassDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String json = "{\"className\":\"java.lang.String\",\"value\":\"Hello World\"}";
        String expected = "Hello World";
        String result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test
    public void testJavaTypeDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        String json = "{\"className\":\"java.lang.Integer\",\"value\":42}";
        Integer expected = 42;
        Integer result = mapper.readValue(json, deserializer.getType());
        assertEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Boolean.class);
        String json = "{\"className\":\"java.lang.Boolean\",\"value\":\"true\"}"; // should be 'true' not '"true"'
        mapper.readValue(json, deserializer.getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"className\":\"java.util.Date\",\"value\":\"2023-03-16\"}";
        mapper.readValue(json, FromStringDeserializer.class);
    }
}