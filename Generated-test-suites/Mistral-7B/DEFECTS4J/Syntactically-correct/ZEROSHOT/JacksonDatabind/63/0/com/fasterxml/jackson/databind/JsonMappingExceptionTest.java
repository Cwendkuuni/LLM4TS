package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JsonMappingExceptionTest {
    @org.junit.Test
    public void testJsonMappingException() {
        // Test default constructor
        JsonMappingException e1 = new JsonMappingException("Message 1");
        assertEquals("Message 1", e1.getMessage());
        assertNull(e1.getPath());

        // Test constructor with message and Throwable
        JsonProcessingException pex1 = new JsonProcessingException("Message 1", new IOException("IO exception"));
        JsonMappingException e2 = new JsonMappingException(pex1);
        assertEquals("Message 1", e2.getMessage());
        assertEquals("IO exception", e2.getCause().getMessage());
        assertNull(e2.getPath());

        // Test constructor with message, location and Throwable
        JsonParser parser = createMockParser();
        JsonMappingException e3 = JsonMappingException.from(parser, "Message 2");
        assertEquals("Message 2", e3.getMessage());
        assertNull(e3.getCause());
        assertEquals(parser, e3.getProcessor());
        assertNull(e3.getPath());

        // Test constructor with message, location, Throwable and path
        ObjectNode node = createMockNode();
        JsonParser parser1 = createMockParser();
        parser1.setErrorCode(JsonParser.CurrentName.FIELD_NAME);
        JsonMappingException e4 = JsonMappingException.from(parser1, "Message 3", node);
        assertEquals("Message 3", e4.getMessage());
        assertEquals(parser1, e4.getProcessor());
        List<JsonMappingException.Reference> path = e4.getPath();
        assertEquals(1, path.size());
        JsonMappingException.Reference ref = path.get(0);
        assertEquals("fieldName", ref.getFieldName());
        assertNull(ref.getFrom());
        assertEquals(JsonParser.CurrentName.FIELD_NAME, ref.getClass().getField("_location").get(ref));

        // Test constructor with message, location, Throwable, path and root cause
        JsonParser parser2 = createMockParser();
        JsonMappingException e5 = JsonMappingException.from(parser2, "Message 4", node, new IOException("IO exception"));
        assertEquals("Message 4", e5.getMessage());
        assertEquals("IO exception", e5.getCause().getMessage());
        assertEquals(parser2, e5.getProcessor());
        assertEquals(1, e5.getPath().size());
        JsonMappingException.Reference ref1 = e5.getPath().get(0);
        assertEquals("fieldName", ref1.getFieldName());
        assertNull(ref1.getFrom());
        assertEquals(JsonParser.CurrentName.FIELD_NAME, ref1.getClass().getField("_location").get(ref1));

        // Test fromJsonProcessingException
        JsonProcessingException pex2 = new JsonProcessingException("Message 5", new IOException("IO exception"));
        JsonMappingException e6 = JsonMappingException.from(pex2);
        assertEquals("Message 5", e6.getMessage());
        assertEquals("IO exception", e6.getCause().getMessage());
        assertNull(e6.getPath());

        // Test fromUnexpectedIOE
        IOException ioex = new IOException("Unexpected IO exception");
        JsonMappingException e7 = JsonMappingException.fromUnexpectedIOE(ioex);
        assertEquals("Unexpected IOException (of type IOException): Unexpected IO exception", e7.getMessage());
        assertNull(e7.getCause());
        assertNull(e7.getPath());

        // Test wrapWithPath
        IOException ioex1 = new IOException("IO exception 1");
        Object refFrom = new Object();
        String refFieldName = "fieldName 1";
        JsonMappingException e8 = JsonMappingException.wrapWithPath(ioex1, refFrom, refFieldName);
        assertEquals("IO exception 1 (was IOException)", e8.getMessage());
        assertNull(e8.getCause());
        List<JsonMappingException.Reference> path1 = e8.getPath();
        assertEquals(1, path1.size());
        JsonMappingException.Reference ref = path1.get(0);
        assertEquals(refFrom, ref.getFrom());
        assertEquals(refFieldName, ref.getFieldName());
        assertNull(ref.getIndex());
        assertEquals("refFrom[refFieldName]", ref.toString());

        Object refFrom1 = new Object[1];
        int refIndex = 0;
        JsonMappingException e9 = JsonMappingException.wrapWithPath(ioex1, refFrom1, refIndex);
        assertEquals("IO exception 1 (was IOException)", e9.getMessage());
        assertNull(e9.getCause());
        List<JsonMappingException.Reference> path2 = e9.getPath();
        assertEquals(1, path2.size());
        JsonMappingException.Reference ref1 = path2.get(0);
        assertEquals(refFrom1, ref1.getFrom());
        assertEquals(refIndex, ref1.getIndex());
        assertEquals("[0]", ref1.toString());

        // Test prependPath
        JsonMappingException e10 = new JsonMappingException("Message 6");
        JsonMappingException e11 = JsonMappingException.wrapWithPath(new IOException("IO exception 2"), e10, "fieldName 2");
        JsonMappingException e12 = JsonMappingException.wrapWithPath(new IOException("IO exception 3"), e11, "fieldName 3");
        List<JsonMappingException.Reference> path3 = e12.getPath();
        assertEquals(3, path3.size());
        JsonMappingException.Reference ref2 = path3.get(0);
        assertEquals("IO exception 3", ref2.getCause().getMessage());
        assertEquals("IO exception 3", ref2.toString());
        JsonMappingException.Reference ref3 = path3.get(1);
        assertEquals("e11", ref3.getFrom().getClass().getSimpleName());
        assertEquals("fieldName 3", ref3.getFieldName());
        assertEquals("e11[fieldName 3]", ref3.toString());
        JsonMappingException.Reference ref4 = path3.get(2);
        assertEquals("e10", ref4.getFrom().getClass().getSimpleName());
        assertEquals("fieldName 2", ref4.getFieldName());
        assertEquals("e10[fieldName 2]", ref4.toString());
    }

    private JsonParser createMockParser() {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCodec()).thenReturn(new ObjectMapper().getFactory().getEncoder(Object.class).getCodec());
        return parser;
    }

    private ObjectNode createMockNode() {
        ObjectNode node = mock(ObjectNode.class);
        when(node.getClass()).thenReturn(ObjectNode.class);
        return node;
    }
}