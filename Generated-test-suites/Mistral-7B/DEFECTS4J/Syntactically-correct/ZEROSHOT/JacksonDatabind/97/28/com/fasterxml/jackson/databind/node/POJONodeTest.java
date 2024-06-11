package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeCreation() {
        Object pojo = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        POJONode node = new POJONode(pojo);
        assertNotNull(node);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testPOJONodeAsText() {
        Object pojo = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals("{\"name\":\"John Doe\",\"age\":30}", node.asText());
    }

    @Test
    public void testPOJONodeAsTextWithDefaultValue() {
        Object pojo = new Object() {{
            name = null;
            age = 30;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals("null", node.asText("defaultValue"));
    }

    @Test
    public void testPOJONodeAsBoolean() {
        Object pojo = new Object() {{
            boolean flag = true;
        }};

        POJONode node = new POJONode(pojo);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testPOJONodeAsBooleanWithDefaultValue() {
        Object pojo = new Object() {};

        POJONode node = new POJONode(pojo);
        assertFalse(node.asBoolean(true));
    }

    @Test
    public void testPOJONodeAsInt() {
        Object pojo = new Object() {{
            int number = 42;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals(42, node.asInt(0));
    }

    @Test
    public void testPOJONodeAsIntWithDefaultValue() {
        Object pojo = new Object() {{
            int number = 0;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals(0, node.asInt(10));
    }

    @Test
    public void testPOJONodeAsLong() {
        Object pojo = new Object() {{
            long number = 42L;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals(42L, node.asLong(0L));
    }

    @Test
    public void testPOJONodeAsLongWithDefaultValue() {
        Object pojo = new Object() {{
            long number = 0L;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals(0L, node.asLong(10L));
    }

    @Test
    public void testPOJONodeAsDouble() {
        Object pojo = new Object() {{
            double number = 42.3;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals(42.3, node.asDouble(0.0), 0.001);
    }

    @Test
    public void testPOJONodeAsDoubleWithDefaultValue() {
        Object pojo = new Object() {{
            double number = 0.0;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals(0.0, node.asDouble(10.0), 0.001);
    }

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        Object pojo = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        POJONode node = new POJONode(pojo);
        String json = mapper.writeValueAsString(node);
        Object jsonObject = mapper.readValue(json, Object.class);

        assertEquals(pojo.getClass(), jsonObject.getClass());
        assertEquals(pojo, jsonObject);
    }

    @Test
    public void testPOJONodeEquals() {
        Object pojo1 = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        Object pojo2 = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        POJONode node1 = new POJONode(pojo1);
        POJONode node2 = new POJONode(pojo2);

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertTrue(node2.equals(node1));
    }

    @Test
    public void testPOJONodeHashCode() {
        Object pojo = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        POJONode node = new POJONode(pojo);
        int hashCode1 = node.hashCode();

        Object pojo2 = new Object() {{
            name = "John Doe";
            age = 30;
        }};
        POJONode node2 = new POJONode(pojo2);

        int hashCode2 = node2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testPOJONodeToString() {
        Object pojo = new Object() {{
            name = "John Doe";
            age = 30;
        }};

        POJONode node = new POJONode(pojo);
        assertEquals("POJONode(John Doe)", node.toString());
    }
}