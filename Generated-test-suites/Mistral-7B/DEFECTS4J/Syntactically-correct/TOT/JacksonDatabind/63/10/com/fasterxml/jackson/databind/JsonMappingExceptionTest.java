package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingExceptionWithSimpleMessage() throws Exception {
        String json = "{}";
        JsonMappingException exception = JsonMappingException.from(mapper.getFactory().createParser(json), "Unexpected token");
        assertEquals("Unexpected token (through reference chain: [])", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndRootCause() throws Exception {
        String json = "{}";
        JsonProcessingException processingException = new JsonProcessingException("Unexpected token", null, mapper.getFactory().createParser(json));
        JsonMappingException exception = JsonMappingException.from(processingException);
        assertEquals("Unexpected token (through reference chain: [])", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocation() throws Exception {
        String json = "{}";
        JsonProcessingException processingException = new JsonProcessingException("Unexpected token", new Location(0, 0), mapper.getFactory().createParser(json));
        JsonMappingException exception = JsonMappingException.from(processingException);
        assertEquals("Unexpected token (through reference chain: [Location@" + Integer.toHexString(System.identityHashCode(processingException.getLocation())) + "])", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithMessageLocationAndRootCause() throws Exception {
        String json = "{}";
        JsonProcessingException processingException = new JsonProcessingException("Unexpected token", new Location(0, 0), mapper.getFactory().createParser(json));
        JsonMappingException exception = JsonMappingException.from(processingException);
        assertEquals("Unexpected token (through reference chain: [Location@" + Integer.toHexString(System.identityHashCode(processingException.getLocation())) + "])", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithProcessor() throws Exception {
        String json = "{}";
        JsonMappingException exception = new JsonMappingException(mapper.getFactory().createParser(json), "Unexpected token");
        assertEquals("Unexpected token (through reference chain: [Parser@" + Integer.toHexString(System.identityHashCode(exception.getProcessor())) + "])", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessage() throws Exception {
        String json = "{}";
        JsonMappingException exception = new JsonMappingException(mapper.getFactory().createParser(json), "Unexpected token: invalid character '{': not recognized as a valid JSON Escape character");
        assertEquals("Unexpected token: invalid character '{': not recognized as a valid JSON Escape character (through reference chain: [Parser@" + Integer.toHexString(System.identityHashCode(exception.getProcessor())) + "])", exception.getPathReference());
    }

    @Test
    public void testJsonMappingExceptionWithProcessorMessageAndRootCause() throws Exception {
        String json = "{}";
        JsonProcessingException processingException = new JsonProcessingException("Unexpected token: invalid character '{': not recognized as a valid JSON Escape character", new Location(0, 0), mapper.getFactory().createParser(json));
        JsonMappingException exception = JsonMappingException.from(processingException);
        assertEquals("Unexpected token: invalid character '{': not recognized as a valid JSON Escape character (through reference chain: [Parser@" + Integer.toHexString(System.identityHashCode(processingException.getParser())) + "])", exception.getPathReference());
    }
}