package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testPOJONode_DefaultConstructor() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        assertNull(node.getPojo());
    }

    @Test
    public void testPOJONode_ConstructorWithObject() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
        assertEquals(testObject, node.getPojo().toString());
    }

    @Test
    public void testGetNodeType() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] binaryData = new byte[]{1, 2, 3};
        POJONode nodeWithBinaryData = new POJONode(binaryData);
        assertEquals(binaryData, nodeWithBinaryData.binaryValue());

        POJONode nodeWithPojo = new POJONode(new Object());
        assertNull(nodeWithPojo.binaryValue());
    }

    @Test
    public void testAsText() {
        String testObject1 = "Test Object 1";
        POJONode node1 = new POJONode(testObject1);
        assertEquals("\"Test Object 1\"", node1.asText());

        String testObject2 = null;
        POJONode node2 = new POJONode(testObject2);
        assertEquals("\"null\"", node2.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        assertEquals(testObject, node.asText("Default Value"));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeWithBooleanValue = new POJONode(true);
        assertTrue(nodeWithBooleanValue.asBoolean(false));

        POJONode nodeWithNullValue = new POJONode(null);
        assertFalse(nodeWithNullValue.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode nodeWithIntValue = new POJONode(42);
        assertEquals(42, nodeWithIntValue.asInt(0));

        POJONode nodeWithNullValue = new POJONode(null);
        assertEquals(0, nodeWithNullValue.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode nodeWithLongValue = new POJONode(42L);
        assertEquals(42L, nodeWithLongValue.asLong(0L));

        POJONode nodeWithNullValue = new POJONode(null);
        assertEquals(0L, nodeWithNullValue.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeWithDoubleValue = new POJONode(3.14);
        assertEquals(3.14, nodeWithDoubleValue.asDouble(0.0), 0.001);

        POJONode nodeWithNullValue = new POJONode(null);
        assertEquals(0.0, nodeWithNullValue.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonGenerator gen = factory.createGenerator(System.out);

        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        node.serialize(gen, new SerializerProvider());

        String expectedJson = String.format("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":%s}", testObject);
        String actualJson = gen.getOutputFactory().getValueAsString();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testGetPojo() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        assertEquals(testObject, node.getPojo());
    }

    @Test
    public void testEquals() {
        String testObject1 = "Test Object 1";
        String testObject2 = "Test Object 2";
        POJONode node1 = new POJONode(testObject1);
        POJONode node2 = new POJONode(testObject2);

        POJONode node1WithSameValue = new POJONode(testObject1);
        POJONode node1WithDifferentValue = new POJONode("Test Object 1 X");

        assertNotEquals(node1, node2);
        assertNotEquals(node1, node1WithDifferentValue);
        assertEquals(node1, node1WithSameValue);
    }

    @Test
    public void testHashCode() {
        String testObject = "Test Object";
        POJONode node1 = new POJONode(testObject);
        POJONode node2 = new POJONode(testObject);

        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);
        assertEquals(String.format("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":%s}", testObject), node.toString());

        POJONode nodeWithBinaryData = new POJONode(new byte[]{1, 2, 3});
        assertEquals(String.format("(binary value of %d bytes)", 3), nodeWithBinaryData.toString());

        POJONode nodeWithRawValue = new POJONode(new RawValue("Test Value"));
        assertEquals(String.format("(raw value '%s')", "Test Value"), nodeWithRawValue.toString());
    }
}