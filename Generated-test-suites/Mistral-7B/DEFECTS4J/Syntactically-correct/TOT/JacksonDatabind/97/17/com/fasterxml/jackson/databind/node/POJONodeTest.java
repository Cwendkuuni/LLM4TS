package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        // Test serialization of a simple POJO
        POJONode node = new POJONode(new SimplePojo("test"));
        JsonNode expectedNode = mapper.valueToTree(new SimplePojo("test"));
        JsonNode actualNode = node.serialize(null, null);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testPOJONodeSerializationWithJsonSerializable() throws JsonProcessingException {
        // Test serialization of a JsonSerializable POJO
        SerializablePojo serializablePojo = new SerializablePojo(1, "test");
        POJONode node = new POJONode(serializablePojo);
        JsonNode expectedNode = mapper.valueToTree(serializablePojo);
        JsonNode actualNode = node.serialize(null, null);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testPOJONodeSerializationWithNullValue() throws JsonProcessingException {
        // Test serialization of a null value
        POJONode node = new POJONode(null);
        JsonNode expectedNode = mapper.valueToTree(null);
        JsonNode actualNode = node.serialize(null, null);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testPOJONodeEquality() {
        // Test POJO nodes equality based on their wrapped POJO
        POJONode node1 = new POJONode(new SimplePojo("test1"));
        POJONode node2 = new POJONode(new SimplePojo("test1"));
        POJONode node3 = new POJONode(new SimplePojo("test2"));
        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testPOJONodeHashCode() {
        // Test POJO nodes hashCode based on their wrapped POJO
        POJONode node1 = new POJONode(new SimplePojo("test1"));
        POJONode node2 = new POJONode(new SimplePojo("test1"));
        POJONode node3 = new POJONode(new SimplePojo("test2"));
        assertEquals(node1.hashCode(), node2.hashCode());
        assertNotSame(node1.hashCode(), node3.hashCode());
    }

    @Test
    public void testPOJONodeToString() {
        // Test toString method of POJONode
        POJONode node = new POJONode(new SimplePojo("test"));
        assertEquals("(value of class com.example.SimplePojo with properties [value=test])", node.toString());
    }

    @Test
    public void testPOJONodeBinaryValue() throws IOException {
        // Test binaryValue method of POJONode
        byte[] binaryData = new byte[]{1, 2, 3};
        POJONode node = new POJONode(binaryData);
        byte[] actualBinaryData = node.binaryValue();
        assertArrayEquals(binaryData, actualBinaryData);
    }

    @Test(expected = IOException.class)
    public void testPOJONodeBinaryValueWithNonByteArrayValue() throws IOException {
        // Test binaryValue method with non-byte array value
        POJONode node = new POJONode(new SimplePojo("test"));
        node.binaryValue();
    }
}