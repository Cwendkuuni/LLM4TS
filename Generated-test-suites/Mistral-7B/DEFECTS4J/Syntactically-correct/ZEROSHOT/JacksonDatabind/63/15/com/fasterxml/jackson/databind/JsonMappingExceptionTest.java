package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test class for {@link JsonMappingException}.
 */
public class JsonMappingExceptionTest {

    /**
     * Test constructor with message.
     */
    @Test
    public void testConstructorWithMessage() {
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    /**
     * Test constructor with message and root cause.
     */
    @Test
    public void testConstructorWithMessageAndRootCause() {
        IOException rootCause = new IOException("Root cause message");
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(message, rootCause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(rootCause, exception.getCause());
    }

    /**
     * Test constructor with message, location, and root cause.
     */
    @Test
    public void testConstructorWithMessageLocationAndRootCause() {
        JsonLocation location = new JsonLocation(1, 1, "Test file", "Test line");
        IOException rootCause = new IOException("Root cause message");
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(message, location, rootCause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(location, exception.getLocation());
        Assert.assertEquals(rootCause, exception.getCause());
    }

    /**
     * Test constructor with processor, message, and root cause.
     */
    @Test
    public void testConstructorWithProcessorMessageAndRootCause() throws IOException {
        Closeable processor = new JsonFactory().createParser(getClass().getResourceAsStream("/test.json")); as JsonParser;
        String message = "Test message";
        IOException rootCause = new IOException("Root cause message");
        JsonMappingException exception = new JsonMappingException(processor, message, rootCause);
        Assert.assertEquals(processor, exception.getProcessor());
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(rootCause, exception.getCause());
        processor.close();
    }

    /**
     * Test constructor with processor, message, and problem.
     */
    @Test
    public void testConstructorWithProcessorMessageAndProblem() throws IOException {
        Closeable processor = new JsonFactory().createParser(getClass().getResourceAsStream("/test.json")); as JsonParser;
        JsonProcessingException problem = new JsonProcessingException(processor, "Test processing exception");
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(processor, message, problem);
        Assert.assertEquals(processor, exception.getProcessor());
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(problem, exception.getCause());
        processor.close();
    }

    /**
     * Test constructor with processor, message, location, and root cause.
     */
    @Test
    public void testConstructorWithProcessorMessageLocationAndRootCause() throws IOException {
        Closeable processor = new JsonFactory().createParser(getClass().getResourceAsStream("/test.json")); as JsonParser;
        JsonLocation location = new JsonLocation(1, 1, "Test file", "Test line");
        IOException rootCause = new IOException("Root cause message");
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(processor, message, location, rootCause);
        Assert.assertEquals(processor, exception.getProcessor());
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(location, exception.getLocation());
        Assert.assertEquals(rootCause, exception.getCause());
        processor.close();
    }

    /**
     * Test factory method fromJsonProcessingException.
     */
    @Test
    public void testFromJsonProcessingException() {
        JsonProcessingException jsonProcessingException = new JsonProcessingException("Test processing exception");
        JsonMappingException jsonMappingException = JsonMappingException.fromJsonProcessingException(jsonProcessingException);
        Assert.assertEquals(jsonProcessingException.getMessage(), jsonMappingException.getMessage());
        Assert.assertEquals(jsonProcessingException.getCause(), jsonMappingException.getCause());
    }

    /**
     * Test factory method fromIOException.
     */
    @Test
    public void testFromIOException() {
        IOException ioException = new IOException("Test I/O exception");
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        Assert.assertEquals("Unexpected IOException (of type IOException): Test I/O exception", jsonMappingException.getMessage());
        Assert.assertEquals(ioException, jsonMappingException.getCause());
    }

    /**
     * Test wrapWithPath method with non-indexed object and field name.
     */
    @Test
    public void testWrapWithPathWithNonIndexedObjectAndFieldName() {
        Object refFrom = new Object() {};
        String refFieldName = "fieldName";
        IOException ioException = new IOException("Test I/O exception");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(ioException, refFrom, refFieldName);
        List<Reference> references = jsonMappingException.getPath();
        Assert.assertEquals(1, references.size());
        Reference reference = references.get(0);
        Assert.assertEquals(refFrom, reference.getFrom());
        Assert.assertEquals(refFieldName, reference.getFieldName());
    }

    /**
     * Test wrapWithPath method with non-indexed object and index.
     */
    @Test
    public void testWrapWithPathWithNonIndexedObjectAndIndex() {
        Object refFrom = new Object() {};
        int refIndex = 0;
        IOException ioException = new IOException("Test I/O exception");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(ioException, refFrom, refIndex);
        List<Reference> references = jsonMappingException.getPath();
        Assert.assertEquals(1, references.size());
        Reference reference = references.get(0);
        Assert.assertEquals(refFrom, reference.getFrom());
        Assert.assertEquals(-1, reference.getIndex());
    }

    /**
     * Test wrapWithPath method with indexed object.
     */
    @Test
    public void testWrapWithPathWithIndexedObject() {
        Object refFrom = new Object[] {new Object(), new Object()}[0];
        int refIndex = 0;
        IOException ioException = new IOException("Test I/O exception");
        JsonMappingException jsonMappingException = JsonMappingException.wrapWithPath(ioException, refFrom, refIndex);
        List<Reference> references = jsonMappingException.getPath();
        Assert.assertEquals(1, references.size());
        Reference reference = references.get(0);
        Assert.assertEquals(refFrom, reference.getFrom());
        Assert.assertEquals(refIndex, reference.getIndex());
    }
}