package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        // Test constructors with no arguments
        JsonMappingException exception1 = new JsonMappingException("Error message 1");
        assertEquals("Error message 1", exception1.getMessage());
        assertEquals(JsonMappingException.class, exception1.getClass());

        JsonMappingException exception2 = new JsonMappingException("Error message 1", new IOException("IO error"));
        assertEquals("Error message 1", exception2.getMessage());
        assertEquals("IO error", exception2.getCause().getMessage());
        assertEquals(JsonMappingException.class, exception2.getClass());
        assertEquals(IOException.class, exception2.getCause().getClass());

        // Test constructors with JsonLocation and no arguments
        JsonLocation location = new JsonLocation(1, 1, "test.json");
        JsonMappingException exception3 = new JsonMappingException(location, "Error message 2");
        assertEquals("Error message 2", exception3.getMessage());
        assertEquals(location, exception3.getLocation());
        assertEquals(JsonMappingException.class, exception3.getClass());

        // Test constructors with JsonLocation and Throwable
        JsonMappingException exception4 = new JsonMappingException(location, "Error message 2", new IOException("IO error"));
        assertEquals("Error message 2", exception4.getMessage());
        assertEquals("IO error", exception4.getCause().getMessage());
        assertEquals(location, exception4.getLocation());
        assertEquals(JsonMappingException.class, exception4.getClass());
        assertEquals(IOException.class, exception4.getCause().getClass());

        // Test constructors with Closeable and String
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException exception5 = new JsonMappingException(processor, "Error message 3");
        assertEquals("Error message 3", exception5.getMessage());
        assertNull(exception5.getLocation());
        assertEquals(JsonMappingException.class, exception5.getClass());
        assertEquals(Closeable.class, exception5.getProcessor().getClass());

        // Test constructors with Closeable, String, and Throwable
        JsonMappingException exception6 = new JsonMappingException(processor, "Error message 3", new IOException("IO error"));
        assertEquals("Error message 3", exception6.getMessage());
        assertEquals("IO error", exception6.getCause().getMessage());
        assertEquals(JsonMappingException.class, exception6.getClass());
        assertEquals(Closeable.class, exception6.getProcessor().getClass());
        assertEquals(IOException.class, exception6.getCause().getClass());

        // Test static factory methods
        JsonMappingException exception7 = JsonMappingException.from(new JsonParser(), "Error message 4");
        assertEquals("Error message 4", exception7.getMessage());
        assertNull(exception7.getLocation());
        assertEquals(JsonMappingException.class, exception7.getClass());
        assertEquals(JsonParser.class, exception7.getProcessor().getClass());

        JsonMappingException exception8 = JsonMappingException.from(new JsonParser(), "Error message 4", new IOException("IO error"));
        assertEquals("Error message 4", exception8.getMessage());
        assertEquals("IO error", exception8.getCause().getMessage());
        assertEquals(JsonMappingException.class, exception8.getClass());
        assertEquals(JsonParser.class, exception8.getProcessor().getClass());
        assertEquals(IOException.class, exception8.getCause().getClass());

        JsonMappingException exception9 = JsonMappingException.from(new JsonGenerator(), "Error message 5");
        assertEquals("Error message 5", exception9.getMessage());
        assertNull(exception9.getLocation());
        assertEquals(JsonMappingException.class, exception9.getClass());
        assertEquals(JsonGenerator.class, exception9.getProcessor().getClass());

        JsonMappingException exception10 = JsonMappingException.from(new JsonGenerator(), "Error message 5", new IOException("IO error"));
        assertEquals("Error message 5", exception10.getMessage());
        assertEquals("IO error", exception10.getCause().getMessage());
        assertEquals(JsonMappingException.class, exception10.getClass());
        assertEquals(JsonGenerator.class, exception10.getProcessor().getClass());
        assertEquals(IOException.class, exception10.getCause().getClass());

        JsonMappingException exception11 = JsonMappingException.from(new DeserializationContext(), "Error message 6");
        assertEquals("Error message 6", exception11.getMessage());
        assertNotNull(exception11.getLocation());
        assertEquals(JsonMappingException.class, exception11.getClass());
        assertNull(exception11.getProcessor());

        JsonMappingException exception12 = JsonMappingException.from(new DeserializationContext(), "Error message 6", new IOException("IO error"));
        assertEquals("Error message 6", exception12.getMessage());
        assertNotNull(exception12.getLocation());
        assertEquals(IOException.class, exception12.getCause().getClass());
        assertNull(exception12.getProcessor());

        JsonMappingException exception13 = JsonMappingException.from(new SerializerProvider(), "Error message 7");
        assertEquals("Error message 7", exception13.getMessage());
        assertNull(exception13.getLocation());
        assertEquals(JsonMappingException.class, exception13.getClass());
        assertNull(exception13.getProcessor());

        JsonMappingException exception14 = JsonMappingException.from(new SerializerProvider(), "Error message 7", new IOException("IO error"));
        assertEquals("Error message 7", exception14.getMessage());
        assertEquals("IO error", exception14.getCause().getMessage());
        assertEquals(JsonMappingException.class, exception14.getClass());
        assertEquals(IOException.class, exception14.getCause().getClass());
        assertNull(exception14.getProcessor());

        // Test fromUnexpectedIOE method
        IOException ioException = new IOException("Unexpected IO exception");
        JsonMappingException exception15 = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO exception", exception15.getMessage());
        assertNull(exception15.getLocation());
        assertNull(exception15.getProcessor());
        assertEquals(IOException.class, exception15.getCause().getClass());

        // Test wrapWithPath methods
        IOException ioException2 = new IOException("Unexpected IO exception");
        Object refFrom = new Object();
        String refFieldName = "fieldName";
        JsonMappingException exception16 = JsonMappingException.wrapWithPath(ioException2, refFrom, refFieldName);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO exception (through reference chain: "+refFrom.getClass().getName()+"(\"fieldName\"))", exception16.getMessage());
        assertNotNull(exception16.getLocation());
        assertNull(exception16.getProcessor());
        assertEquals(IOException.class, exception16.getCause().getClass());

        JsonMappingException exception17 = JsonMappingException.from(new JsonParser(), "Error message 8");
        JsonMappingException exception18 = JsonMappingException.wrapWithPath(exception17, refFrom, refFieldName);
        assertEquals("Error message 8 (through reference chain: "+refFrom.getClass().getName()+"(\"fieldName\"))", exception18.getMessage());
        assertNotNull(exception18.getLocation());
        assertEquals(JsonParser.class, exception18.getProcessor().getClass());
        assertEquals(JsonMappingException.class, exception18.getCause().getClass());

        JsonMappingException exception19 = JsonMappingException.from(new JsonParser(), "Error message 9", new IOException("IO error"));
        JsonMappingException exception20 = JsonMappingException.wrapWithPath(exception19, refFrom, refFieldName);
        assertEquals("Error message 9 (through reference chain: "+refFrom.getClass().getName()+"(\"fieldName\"))", exception20.getMessage());
        assertNotNull(exception20.getLocation());
        assertEquals(JsonParser.class, exception20.getProcessor().getClass());
        assertEquals(IOException.class, exception20.getCause().getClass());

        JsonMappingException exception21 = JsonMappingException.from(new JsonParser(), "Error message 10", new IOException("IO error"), new Object[0]);
        JsonMappingException exception22 = JsonMappingException.wrapWithPath(exception21, refFrom, 0);
        assertEquals("Error message 10 (through reference chain: "+refFrom.getClass().getName()+"[0])", exception22.getMessage());
        assertNotNull(exception22.getLocation());
        assertEquals(JsonParser.class, exception22.getProcessor().getClass());
        assertEquals(JsonMappingException.class, exception22.getCause().getClass());
    }

    @Test
    public void testJsonMappingExceptionToString() {
        JsonMappingException exception = new JsonMappingException(new JsonParser(), "Error message 1");
        String expected = "JsonMappingException: Error message 1 (through reference chain: [])";
        String actual = exception.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testJsonMappingExceptionWriteReplace() {
        JsonMappingException exception = new JsonMappingException(new JsonParser(), "Error message 1");
        Reference reference = new Reference(new Object(), "fieldName");
        JsonMappingException newException = (JsonMappingException) exception.writeReplace(reference);
        String expected = "JsonMappingException: Error message 1 (through reference chain: Object(\"fieldName\"))";
        String actual = newException.getMessage();
        assertEquals(expected, actual);
    }
}