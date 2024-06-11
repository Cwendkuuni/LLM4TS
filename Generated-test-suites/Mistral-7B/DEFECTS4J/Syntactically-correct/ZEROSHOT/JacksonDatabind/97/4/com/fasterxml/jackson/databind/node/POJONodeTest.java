package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test class for POJONode.
 *
 * @author John Doe
 */
public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValueNull() throws IOException {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());
    }

    @Test
    public void testBinaryValueByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        POJONode node = new POJONode(data);
        assertArrayEquals(data, node.binaryValue());
    }

    @Test
    public void testAsTextNull() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextString() {
        POJONode node = new POJONode("test");
        assertEquals("test", node.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeTrue = new POJONode(true);
        POJONode nodeFalse = new POJONode(false);
        POJONode nodeNull = new POJONode(null);

        assertTrue(nodeTrue.asBoolean(false));
        assertFalse(nodeFalse.asBoolean(true));
        assertEquals(false, nodeNull.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode nodeOne = new POJONode(1);
        POJONode nodeMinusOne = new POJONode(-1);
        POJONode nodeNull = new POJONode(null);

        assertEquals(1, nodeOne.asInt(0));
        assertEquals(-1, nodeMinusOne.asInt(0));
        assertEquals(0, nodeNull.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode nodeOne = new POJONode(1L);
        POJONode nodeMinusOne = new POJONode(-1L);
        POJONode nodeNull = new POJONode(null);

        assertEquals(1L, nodeOne.asLong(0L));
        assertEquals(-1L, nodeMinusOne.asLong(0L));
        assertEquals(0L, nodeNull.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeOne = new POJONode(1.0);
        POJONode nodeMinusOne = new POJONode(-1.0);
        POJONode nodeNull = new POJONode(null);

        assertEquals(1.0, nodeOne.asDouble(0.0), 0.001);
        assertEquals(-1.0, nodeMinusOne.asDouble(0.0), 0.001);
        assertEquals(0.0, nodeNull.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode nodeValue = new POJONode(new Object());

        StringJsonNode expectedJson = mapper.valueToTree(new Object());

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        nodeValue.serialize(generator, null);
        String actualJson = generator.getOutputContent().toString();

        assertEquals(expectedJson.toString(), actualJson);
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);
        POJONode node3 = new POJONode(obj1);

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node3));
        assertFalse(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();

        POJONode node1 = new POJONode(obj);
        POJONode node2 = new POJONode(obj);
        Object other = new Object();

        assertEquals(obj.hashCode(), node1.hashCode());
        assertEquals(obj.hashCode(), node2.hashCode());
        assertNotEquals(obj.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "POJONode{_value=null}";
        POJONode node = new POJONode(null);
        assertEquals(expected, node.toString());

        expected = "POJONode{_value=TestClass{}}";
        Object testObj = new TestClass();
        POJONode nodeWithValue = new POJONode(testObj);
        assertEquals(expected, nodeWithValue.toString());
    }
}

class TestClass {
    // Add any necessary fields and methods for testing purposes.
}