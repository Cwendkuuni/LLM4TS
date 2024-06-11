package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for JsonMappingException.
 */
public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Test for default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        JsonMappingException e = new JsonMappingException();
        assertNull(e.getMessage());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for constructor with message.
     */
    @Test
    public void testMessageConstructor() {
        JsonMappingException e = new JsonMappingException("Test message");
        assertEquals("Test message", e.getMessage());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for constructor with message and Throwable.
     */
    @Test
    public void testMessageAndThrowableConstructor() {
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException("Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(cause, e.getCause());
        assertNull(e.getFrom());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for constructor with message, JsonLocation and Throwable.
     */
    @Test
    public void testMessageLocationAndThrowableConstructor() {
        JsonLocation loc = new JsonLocation(0, 0, "Test file", "Test line");
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException("Test message", loc, cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(loc, e.getLocation());
        assertEquals(cause, e.getCause());
        assertNull(e.getFrom());
        assertNotNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for constructor with Closeable processor, message and Throwable.
     */
    @Test
    public void testProcessorConstructor() throws IOException {
        Closeable processor = mapper.getFactory().createParser("[]");
        String msg = "Test message";
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException(processor, msg, cause);
        assertEquals(msg, e.getMessage());
        assertEquals(processor, e.getProcessor());
        assertNull(e.getLocation());
        assertNotNull(e.getPath());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().getFirst();
        assertEquals("[", ref.getFrom().getClass().getName());
        assertEquals("root", ref.getFieldName());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from JsonParser.
     */
    @Test
    public void testFromJsonParser() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        String msg = "Test message";
        JsonMappingException e = JsonMappingException.from(parser, msg);
        assertEquals(msg, e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        assertNotNull(e.getPath());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().getFirst();
        assertEquals("[", ref.getFrom().getClass().getName());
        assertEquals("root", ref.getFieldName());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from JsonGenerator.
     */
    @Test
    public void testFromJsonGenerator() {
        JsonMappingException e = JsonMappingException.from(null, "Test message");
        assertNotNull(e);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from JsonGenerator, message and Throwable.
     */
    @Test
    public void testFromJsonGeneratorWithMessageAndThrowable() {
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = JsonMappingException.from(null, "Test message", cause);
        assertNotNull(e);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNotNull(e.getPath());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().getFirst();
        assertEquals("root", ref.getFrom().getClass().getName());
        assertEquals("root", ref.getFieldName());
        assertEquals(cause, e.getCause());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from DeserializationContext.
     */
    @Test
    public void testFromDeserializationContext() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        String msg = "Test message";
        JsonMappingException e = JsonMappingException.from(ctxt, msg);
        assertEquals(msg, e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        assertNotNull(e.getPath());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().getFirst();
        assertEquals("[", ref.getFrom().getClass().getName());
        assertEquals("root", ref.getFieldName());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from DeserializationContext, message and Throwable.
     */
    @Test
    public void testFromDeserializationContextWithMessageAndThrowable() throws IOException {
        JsonParser parser = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = JsonMappingException.from(ctxt, "Test message", cause);
        assertEquals("Test message", e.getMessage());
        assertEquals(parser, e.getProcessor());
        assertNull(e.getLocation());
        assertNotNull(e.getPath());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().getFirst();
        assertEquals("[", ref.getFrom().getClass().getName());
        assertEquals("root", ref.getFieldName());
        assertEquals(cause, e.getCause());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from SerializerProvider.
     */
    @Test
    public void testFromSerializerProvider() {
        JsonMappingException e = JsonMappingException.from(null, "Test message");
        assertNotNull(e);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from SerializerProvider, message and Throwable.
     */
    @Test
    public void testFromSerializerProviderWithMessageAndThrowable() {
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = JsonMappingException.from(null, "Test message", cause);
        assertNotNull(e);
        assertEquals("Test message", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNotNull(e.getPath());
        assertEquals(1, e.getPath().size());
        Reference ref = e.getPath().getFirst();
        assertEquals("root", ref.getFrom().getClass().getName());
        assertEquals("root", ref.getFieldName());
        assertEquals(cause, e.getCause());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for factory method from IOException.
     */
    @Test
    public void testFromIOException() {
        IOException io = new IOException("Test IOException");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(io);
        assertEquals("Unexpected IOException (of type IOException): Test IOException", e.getMessage());
        assertNull(e.getProcessor());
        assertNull(e.getLocation());
        assertNull(e.getPath());
        assertFalse(e.isParsing());
        assertFalse(e.isSerialization());
    }

    /**
     * Test for wrapWithPath method with non-indexed object and field name.
     */
    @Test
    public void testWrapWithPathWithNonIndexedObjectAndFieldName() {
        Object obj = new Object() {{
            String field1 = "field1";
        }};
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException("Test message");
        Reference ref = new Reference(obj, "field1");
        JsonMappingException wrappedE = JsonMappingException.wrapWithPath(cause, obj, "field1");
        assertEquals(1, wrappedE.getPath().size());
        assertEquals(obj, wrappedE.getPath().getFirst().getFrom());
        assertEquals("field1", wrappedE.getPath().getFirst().getFieldName());
        assertEquals(cause, wrappedE.getCause());
        assertEquals(e.getMessage(), wrappedE.getMessage());
        assertEquals(e, wrappedE.getCause());
        assertFalse(wrappedE.isParsing());
        assertFalse(wrappedE.isSerialization());
    }

    /**
     * Test for wrapWithPath method with non-indexed object and null field name.
     */
    @Test(expected = NullPointerException.class)
    public void testWrapWithPathWithNonIndexedObjectAndNullFieldName() {
        Object obj = new Object() {{
            String field1 = "field1";
        }};
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException("Test message");
        JsonMappingException.wrapWithPath(cause, obj, null);
    }

    /**
     * Test for wrapWithPath method with indexed object and index.
     */
    @Test
    public void testWrapWithPathWithIndexedObjectAndIndex() {
        Object obj = new Object[] {1, 2, 3};
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException("Test message");
        Reference ref = new Reference(obj, 1);
        JsonMappingException wrappedE = JsonMappingException.wrapWithPath(cause, obj, 1);
        assertEquals(1, wrappedE.getPath().size());
        assertEquals(obj, wrappedE.getPath().getFirst().getFrom());
        assertEquals("1", wrappedE.getPath().getFirst().getIndex().toString());
        assertEquals(cause, wrappedE.getCause());
        assertEquals(e.getMessage(), wrappedE.getMessage());
        assertEquals(e, wrappedE.getCause());
        assertFalse(wrappedE.isParsing());
        assertFalse(wrappedE.isSerialization());
    }

    /**
     * Test for wrapWithPath method with indexed object and negative index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrapWithPathWithIndexedObjectAndNegativeIndex() {
        Object obj = new Object[] {1, 2, 3};
        Throwable cause = new IllegalArgumentException("Test cause");
        JsonMappingException e = new JsonMappingException("Test message");
        JsonMappingException.wrapWithPath(cause, obj, -1);
    }
}