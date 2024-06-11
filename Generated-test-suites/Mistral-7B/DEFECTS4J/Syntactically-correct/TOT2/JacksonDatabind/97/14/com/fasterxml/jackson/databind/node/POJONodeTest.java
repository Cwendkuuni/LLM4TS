package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONode_NullValue() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("null", node.asText());
        assertEquals("null", node.asText("default"));
        assertFalse(node.asBoolean(false));
        assertFalse(node.asBoolean(true));
        assertFalse(node.equals(new Object()));
        assertFalse(node.equals(new POJONode(new Object())));
        assertArrayEquals(new byte[0], node.binaryValue());
        try {
            node.serialize(null, null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPOJONode_SimpleValue() {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(42);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("42", node.asText());
        assertEquals("42", node.asText("default"));
        assertTrue(node.asBoolean(false));
        assertTrue(node.asBoolean(true));
        assertEquals(42, node.asInt(0));
        assertEquals(42, node.asInt(42));
        assertEquals(42L, node.asLong(0L));
        assertEquals(42L, node.asLong(42L));
        assertEquals(42.0, node.asDouble(0.0), 0.0);
        assertEquals(42.0, node.asDouble(42.0), 0.0);
        assertEquals(node.getPojo(), 42);
        try {
            node.serialize(null, null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPOJONode_ByteArrayValue() {
        byte[] value = new byte[] {1, 2, 3};
        POJONode node = new POJONode(value);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("(binary value of 3 bytes)", node.asText());
        assertFalse(node.asBoolean(false));
        assertFalse(node.asBoolean(true));
        assertFalse(node.equals(new Object()));
        assertFalse(node.equals(new POJONode(new Object())));
        assertArrayEquals(value, node.binaryValue());
        try {
            node.serialize(null, null);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPOJONode_JsonSerializableValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object jsonSerializableValue = new MyJsonSerializableClass();
        POJONode node = new POJONode(jsonSerializableValue);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", node.asText());
        assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", node.asText("default"));
        assertTrue(node.asBoolean(false));
        assertTrue(node.asBoolean(true));
        assertEquals(jsonSerializableValue, node.getPojo());
        JsonNode expectedJsonNode = mapper.readTree(mapper.writeValueAsString(jsonSerializableValue));
        JsonNode actualJsonNode = mapper.readTree(node.asText());
        assertEquals(expectedJsonNode, actualJsonNode);
    }

    
    // Add more test cases here for edge cases and exception handling scenarios
    
}

class MyJsonSerializableClass implements JsonSerializable {
    // Add fields and methods as needed
}