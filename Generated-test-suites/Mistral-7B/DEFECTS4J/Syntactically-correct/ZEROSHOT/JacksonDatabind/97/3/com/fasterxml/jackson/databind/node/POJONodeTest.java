package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

/**
 * Test class for POJONode.java
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
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        byte[] result = node.binaryValue();
        assertArrayEquals(new byte[0], result);

        node = new POJONode("Test");
        assertNull(node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(new Object());
        assertEquals("null", node.asText());

        node = new POJONode("Test");
        assertEquals("Test", node.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("DefaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));

        node = new POJONode(false);
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));

        node = new POJONode(new Integer(2));
        assertEquals(2, node.asInt(0));

        node = new POJONode(new Double(3.5));
        assertEquals(0, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(1L);
        assertEquals(1L, node.asLong(0L));

        node = new POJONode(new Long(2L));
        assertEquals(2L, node.asLong(0L));

        node = new POJONode(new Double(3.5));
        assertEquals(0L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(1.0);
        assertEquals(1.0, node.asDouble(0.0), 0.001);

        node = new POJONode(new Double(2.5));
        assertEquals(2.5, node.asDouble(0.0), 0.001);

        node = new POJONode(new Integer(3));
        assertEquals(3.0, node.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        POJONode node = new POJONode(new Object());

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        JsonParser parser = mapper.getFactory().createParser(json);

        JsonNode result = parser.readValueAs(JsonNode.class);
        assertEquals(JsonNodeType.POJO, result.getNodeType());
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node.getPojo().getClass(), node.getPojo().getClass());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());

        POJONode node3 = new POJONode("Test");
        POJONode node4 = new POJONode("Test");

        POJONode node5 = new POJONode(node1);
        POJONode node6 = new POJONode(node2);

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node5));
        assertTrue(node1.equals(node2)); // due to _pojoEquals implementation

        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(node4));
        assertFalse(node1.equals(null));
        assertFalse(node1.equals("Test"));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();

        assertEquals(hashCode1, node1.hashCode());
        assertEquals(hashCode1, node5.hashCode());
        assertEquals(hashCode1, node2.hashCode()); // due to _pojoEquals implementation

        assertNotEquals(hashCode1, node3.hashCode());
        assertNotEquals(hashCode1, node4.hashCode());
        assertNotEquals(hashCode1, null);
        assertNotEquals(hashCode1, "Test");
    }
}