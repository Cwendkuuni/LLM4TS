package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testSimpleMappingException() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue("{'data': null}", Data.class);
            fail("Expected mapping exception");
        } catch (JsonMappingException e) {
            assertEquals("Root element 'data' must not be null", e.getMessage());
            assertNull(e.getProcessor());
            assertNull(e.getPath());
        }
    }

    @Test
    public void testMappingExceptionWithPath() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode invalidNode = mapper.readTree("{'data': {'inner': null}}");

        try {
            mapper.treeToValue(invalidNode, Data.class);
            fail("Expected mapping exception");
        } catch (JsonMappingException e) {
            assertEquals("Path: /data/inner", e.getPathReference());
            assertEquals("Data.inner should not be null", e.getMessage());
            assertNotNull(e.getProcessor());
            assertEquals(JsonParser.class, e.getProcessor().getClass());
        }
    }

    @Test
    public void testMappingExceptionWithProcessor() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{'data': null}");

        try {
            mapper.readValue(parser, Data.class);
            fail("Expected mapping exception");
        } catch (JsonMappingException e) {
            assertEquals("Root element 'data' must not be null", e.getMessage());
            assertSame(parser, e.getProcessor());
            assertNull(e.getPath());
        }
    }

    @Test
    public void testMappingExceptionWithPathAndProcessor() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode invalidNode = mapper.readTree("{'data': {'inner': {'bad': null}}}");

        try {
            mapper.treeToValue(invalidNode, Data.class);
            fail("Expected mapping exception");
        } catch (JsonMappingException e) {
            assertEquals("Path: /data/inner/bad", e.getPathReference());
            assertEquals("Data.inner.bad should not be null", e.getMessage());
            assertNotNull(e.getProcessor());
            assertEquals(JsonParser.class, e.getProcessor().getClass());
        }
    }

    @Test
    public void testMappingExceptionWithCustomSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class) {};
        mapper.registerSerializer(String.class, stringSerializer);

        try {
            mapper.readValue("{'data': 'null'", Data.class);
            fail("Expected mapping exception");
        } catch (JsonMappingException e) {
            assertEquals("Root element 'data' value cannot be converted to Data (class com.fasterxml.jackson.databind.test.Data) (need to define a JsonSerializer for com.fasterxml.jackson.databind.test.Data)", e.getMessage());
            assertNotNull(e.getProcessor());
            assertSame(JsonParser.class, e.getProcessor().getClass());
        }
    }

    static class Data {
        private String inner;
    }
}