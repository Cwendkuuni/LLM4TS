package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Test;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeWithNullValue() throws JsonProcessingException {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_NULL, node.asToken());
        assertNull(node.getPojo());
        node.serialize(mockSerializerProvider(), null); // should not throw an exception
        assertEquals("null", node.asText());
        assertEquals("null", node.asText("defaultValue"));
        assertFalse(node.asBoolean(false));
        assertFalse(node.asBoolean(true));
        assertFalse(node.asInt(0));
        assertFalse(node.asInt(1));
        assertFalse(node.asLong(0L));
        assertFalse(node.asLong(1L));
        assertFalse(node.asDouble(0.0));
        assertFalse(node.asDouble(1.0));
    }

    @Test
    public void testPOJONodeWithPrimitiveValue() throws JsonProcessingException {
        POJONode node = new POJONode(true);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_BOOL, node.asToken());
        assertTrue(node.getPojo());
        node.serialize(mockSerializerProvider(), null); // should not throw an exception
        assertEquals("true", node.asText());
        assertEquals("defaultValue", node.asText("defaultValue"));
        assertTrue(node.asBoolean(false));
        assertTrue(node.asBoolean(true));
        assertEquals(1, node.asInt(0));
        assertEquals(1, node.asInt(1));
        assertEquals((long) 1, node.asLong(0L));
        assertEquals((long) 1, node.asLong(1L));
        assertEquals(1.0, node.asDouble(0.0), 0.0);
        assertEquals(1.0, node.asDouble(1.0), 0.0);
    }

    @Test
    public void testPOJONodeWithObjectValue() throws JsonProcessingException {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_OBJECT, node.asToken());
        assertNotNull(node.getPojo());
        node.serialize(mockSerializerProvider(), null); // should not throw an exception
        // The asText() and asText(String) methods should call the toString() method of the wrapped Object
        String objectToString = new Object(){}.toString();
        assertEquals(objectToString, node.asText());
        assertEquals("defaultValue", node.asText("defaultValue"));
        // Testing the asBoolean() method with a boolean object
        assertTrue(node.asBoolean(false));
        assertTrue(node.asBoolean(new Boolean(true)));
        // Testing the asInt() method with a Number object
        Number number = 1;
        assertEquals(number.intValue(), node.asInt(0));
        assertEquals(number.intValue(), node.asInt(node));
        // Testing the asLong() method with a Number object
        assertEquals(number.longValue(), node.asLong(0L));
        assertEquals(number.longValue(), node.asLong(node));
        // Testing the asDouble() method with a Number object
        assertEquals(number.doubleValue(), node.asDouble(0.0), 0.0);
        assertEquals(number.doubleValue(), node.asDouble(node), 0.0);
    }

    @Test
    public void testPOJONodeWithByteArrayValue() throws JsonProcessingException {
        POJONode node = new POJONode(new byte[0]);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        assertArrayEquals(new byte[0], node.binaryValue());
        node.serialize(mockSerializerProvider(), null); // should not throw an exception
        assertEquals("[]", node.asText());
        assertEquals("defaultValue", node.asText("defaultValue"));
        assertFalse(node.asBoolean(false));
        assertFalse(node.asBoolean(true));
        assertFalse(node.asInt(0));
        assertFalse(node.asInt(1));
        assertFalse(node.asLong(0L));
        assertFalse(node.asLong(1L));
        assertFalse(node.asDouble(0.0));
        assertFalse(node.asDouble(1.0));
    }

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        Object pojo = new Object();
        POJONode node = new POJONode(pojo);
        String json = mapper.writeValueAsString(node);
        Object deserializedPojo = mapper.readValue(json, Object.class);
        assertEquals(pojo, deserializedPojo);
    }

    private SerializerProvider mockSerializerProvider() {
        return new SerializerProvider() {
            @Override public void writeValue(JsonGenerator gen, Object value) {}
            @Override public void close() {}
            @Override public void setAttribute(String name, Object value, Object parent) {}
        };
    }
}