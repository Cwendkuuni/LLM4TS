package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

    @Test
    public void testPOJONodeWithStringValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode("Hello World");

        // Test getNodeType() and asToken()
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_STRING, node.asToken());

        // Test serialization
        String json = mapper.writeValueAsString(node);
        assertEquals("\"Hello World\"", json);

        // Test asText()
        assertEquals("Hello World", node.asText());
        assertEquals("Default Value", node.asText("Default Value"));

        // Test asBoolean()
        assertFalse(node.asBoolean(false));

        // Test asInt() and asLong()
        assertEquals(42, node.asInt(42));
        assertEquals(42L, node.asLong(42L));

        // Test asDouble()
        assertEquals(3.14, node.asDouble(3.14), 0.001);

        // Test equals()
        POJONode other = new POJONode("Hello World");
        assertTrue(node.equals(other));

        // Test hashCode()
        int hashCode1 = node.hashCode();
        POJONode otherNode = new POJONode("Hello World");
        int hashCode2 = other.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testPOJONodeWithBooleanValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object value = true;
        POJONode node = new POJONode(value);

        // Test getNodeType() and asToken()
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_BOOLEAN, node.asToken());

        // Test serialization
        String json = mapper.writeValueAsString(node);
        assertEquals("true", json);

        // Test asBoolean()
        assertTrue(node.asBoolean(false));

        // Test asInt(), asLong(), and asDouble()
        assertEquals(1, node.asInt(1));
        assertEquals(1L, node.asLong(1L));
        assertEquals(1.0, node.asDouble(1.0), 0.001);

        // Test equals() and hashCode()
        POJONode other = new POJONode(value);
        assertTrue(node.equals(other));
        assertEquals(node.hashCode(), other.hashCode());
    }

    @Test
    public void testPOJONodeWithNumberValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object value = 123;
        POJONode node = new POJONode(value);

        // Test getNodeType() and asToken()
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_NUMBER_INT, node.asToken());

        // Test serialization
        String json = mapper.writeValueAsString(node);
        assertEquals("123", json);

        // Test asInt(), asLong(), and asDouble()
        assertEquals(123, node.asInt(123));
        assertEquals(123L, node.asLong(123L));
        assertEquals(123.0, node.asDouble(123.0), 0.001);

        // Test equals() and hashCode()
        POJONode other = new POJONode(value);
        assertTrue(node.equals(other));
        assertEquals(node.hashCode(), other.hashCode());
    }

    @Test
    public void testPOJONodeWithNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object value = null;
        POJONode node = new POJONode(value);

        // Test getNodeType() and asToken()
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_NULL, node.asToken());

        // Test serialization
        String json = mapper.writeValueAsString(node);
        assertEquals("null", json);

        // Test asText() and asBoolean()
        assertEquals("null", node.asText());
        assertEquals(false, node.asBoolean(false));

        // Test asInt(),
        assertNull(node.asInt(0));

        // Test asLong() and asDouble()
        assertNull(node.asLong(0L));
        assertEquals(0.0, node.asDouble(0.0), 0.001);

        // Test equals() and hashCode()
        POJONode other = new POJONode(value);
        assertTrue(node.equals(other));
        assertEquals(node.hashCode(), other.hashCode());
    }

    @Test
    public void testPOJONodeWithCustomClassValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        class TestClass {
            private String name;

            public TestClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestClass value = new TestClass("Test");
        POJONode node = new POJONode(value);

        // Test getNodeType() and asToken()
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());

        // Test serialization
        String json = mapper.writeValueAsString(node);
        TestClass deserializedValue = mapper.readValue(json, TestClass.class);
        assertEquals(value.getName(), deserializedValue.getName());

        // Test asText()
        assertEquals("Test", node.asText());

        // Test asBoolean()
        assertFalse(node.asBoolean(false));

        // Test asInt(), asLong(), and asDouble()
        assertNull(node.asInt(0));
        assertNull(node.asLong(0L));
        assertEquals(0.0, node.asDouble(0.0), 0.001);

        // Test equals() and hashCode()
        POJONode other = new POJONode(value);
        assertTrue(node.equals(other));
        assertEquals(node.hashCode(), other.hashCode());
    }

    @Test(expected = IOException.class)
    public void testPOJONodeWithByteArrayValueThrowsIOException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] value = { 1, 2, 3 };
        POJONode node = new POJONode(value);
        node.binaryValue();
    }
}