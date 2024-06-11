package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

/**
 * Test class for POJONode.java
 */
public class POJONodeTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test constructor POJONode with null value.
     */
    @Test
    public void testPOJONodeWithNullValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("null", pojoNode.asText());
        assertTrue(pojoNode.equals(pojoNode));
        assertFalse(pojoNode.equals(new TextNode("test")));
        assertNull(pojoNode.getPojo());
    }

    /**
     * Test constructor POJONode with simple POJO value.
     */
    @Test
    public void testPOJONodeWithSimplePOJOValue() {
        String testValue = "test";
        POJONode pojoNode = new POJONode(testValue);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals(JsonToken.VALUE_STRING, pojoNode.asToken());
        assertEquals("test", pojoNode.asText());
        assertEquals("test", pojoNode.asText(null));
        assertTrue(pojoNode.equals(pojoNode));
        assertFalse(pojoNode.equals(new TextNode("differentValue")));
        assertEquals(testValue, pojoNode.getPojo());
    }

    /**
     * Test constructor POJONode with complex POJO value.
     */
    @Test
    public void testPOJONodeWithComplexPOJOValue() throws JsonMappingException, JsonGenerationException {
        String testValue = "test";
        TestPojo testPojo = new TestPojo(testValue);
        POJONode pojoNode = new POJONode(testPojo);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
        assertEquals("{\"value\":\"" + testValue + "\"}", pojoNode.asText());
        assertEquals("{\"value\":\"" + testValue + "\"}", pojoNode.asText(null));
        assertTrue(pojoNode.equals(pojoNode));
        assertFalse(pojoNode.equals(new TextNode("differentValue")));
        assertEquals(testPojo, pojoNode.getPojo());
    }

    /**
     * Test method asText with default value.
     */
    @Test
    public void testAsTextWithDefaultValue() {
        String testValue = "test";
        POJONode pojoNode = new POJONode(testValue);
        assertEquals("test", pojoNode.asText(null));
    }

    /**
     * Test method asText with custom default value.
     */
    @Test
    public void testAsTextWithCustomDefaultValue() {
        String testValue = "test";
        POJONode pojoNode = new POJONode(null);
        assertEquals("defaultValue", pojoNode.asText("defaultValue"));
    }

    /**
     * Test method asBoolean with default value.
     */
    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertFalse(pojoNode.asBoolean(false));
    }

    /**
     * Test method asBoolean with POJO value that can be casted to boolean.
     */
    @Test
    public void testAsBooleanWithBooleanPOJONodeValue() {
        boolean testValue = true;
        POJONode pojoNode = new POJONode(testValue);
        assertTrue(pojoNode.asBoolean(false));
    }

    /**
     * Test method asBoolean with POJO value that cannot be casted to boolean.
     */
    @Test
    public void testAsBooleanWithNonBooleanPOJONodeValue() {
        TestPojo testPojo = new TestPojo();
        POJONode pojoNode = new POJONode(testPojo);
        assertFalse(pojoNode.asBoolean(false));
    }

    /**
     * Test method asInt with default value.
     */
    @Test
    public void testAsIntWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0, pojoNode.asInt(0));
    }

    /**
     * Test method asInt with POJO value that can be casted to int.
     */
    @Test
    public void testAsIntWithIntPOJONodeValue() {
        int testValue = 123;
        POJONode pojoNode = new POJONode(testValue);
        assertEquals(testValue, pojoNode.asInt(0));
    }

    /**
     * Test method asInt with POJO value that cannot be casted to int.
     */
    @Test
    public void testAsIntWithNonIntPOJONodeValue() {
        TestPojo testPojo = new TestPojo();
        POJONode pojoNode = new POJONode(testPojo);
        assertEquals(0, pojoNode.asInt(0));
    }

    /**
     * Test method asLong with default value.
     */
    @Test
    public void testAsLongWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0L, pojoNode.asLong(0L));
    }

    /**
     * Test method asLong with POJO value that can be casted to long.
     */
    @Test
    public void testAsLongWithLongPOJONodeValue() {
        long testValue = 1234567890L;
        POJONode pojoNode = new POJONode(testValue);
        assertEquals(testValue, pojoNode.asLong(0L));
    }

    /**
     * Test method asLong with POJO value that cannot be casted to long.
     */
    @Test
    public void testAsLongWithNonLongPOJONodeValue() {
        TestPojo testPojo = new TestPojo();
        POJONode pojoNode = new POJONode(testPojo);
        assertEquals(0L, pojoNode.asLong(0L));
    }

    /**
     * Test method asDouble with default value.
     */
    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);
    }

    /**
     * Test method asDouble with POJO value that can be casted to double.
     */
    @Test
    public void testAsDoubleWithDoublePOJONodeValue() {
        double testValue = 123.456;
        POJONode pojoNode = new POJONode(testValue);
        assertEquals(testValue, pojoNode.asDouble(0.0), 0.001);
    }

    /**
     * Test method asDouble with POJO value that cannot be casted to double.
     */
    @Test
    public void testAsDoubleWithNonDoublePOJONodeValue() {
        TestPojo testPojo = new TestPojo();
        POJONode pojoNode = new POJONode(testPojo);
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);
    }

    /**
     * Test method equals with same POJONode instance.
     */
    @Test
    public void testEqualsWithSameInstance() {
        String testValue = "test";
        POJONode pojoNode1 = new POJONode(testValue);
        POJONode pojoNode2 = pojoNode1;
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    /**
     * Test method equals with different POJONode instance having the same POJO value.
     */
    @Test
    public void testEqualsWithDifferentInstanceButSamePOJOValue() {
        String testValue = "test";
        POJONode pojoNode1 = new POJONode(testValue);
        POJONode pojoNode2 = new POJONode(testValue);
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    /**
     * Test method equals with different POJONode instance having different POJO value.
     */
    @Test
    public void testEqualsWithDifferentInstanceButDifferentPOJOValue() {
        String testValue1 = "test1";
        String testValue2 = "test2";
        POJONode pojoNode1 = new POJONode(testValue1);
        POJONode pojoNode2 = new POJONode(testValue2);
        assertFalse(pojoNode1.equals(pojoNode2));
    }

    /**
     * Test method equals with different node type.
     */
    @Test
    public void testEqualsWithDifferentNodeType() {
        String testValue = "test";
        POJONode pojoNode = new POJONode(testValue);
        TextNode textNode = new TextNode(testValue);
        assertFalse(pojoNode.equals(textNode));
    }

    /**
     * Test method hashCode with same POJONode instance.
     */
    @Test
    public void testHashCodeWithSameInstance() {
        String testValue = "test";
        POJONode pojoNode = new POJONode(testValue);
        int hashCode1 = pojoNode.hashCode();
        POJONode pojoNode2 = pojoNode;
        int hashCode2 = pojoNode2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test method hashCode with different POJONode instances having the same POJO value.
     */
    @Test
    public void testHashCodeWithDifferentInstancesButSamePOJOValue() {
        String testValue = "test";
        POJONode pojoNode1 = new POJONode(testValue);
        POJONode pojoNode2 = new POJONode(testValue);
        int hashCode1 = pojoNode1.hashCode();
        int hashCode2 = pojoNode2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    /**
     * Test method hashCode with different POJONode instances having different POJO values.
     */
    @Test
    public void testHashCodeWithDifferentInstancesButDifferentPOJOValue() {
        String testValue1 = "test1";
        String testValue2 = "test2";
        POJONode pojoNode1 = new POJONode(testValue1);
        POJONode pojoNode2 = new POJONode(testValue2);
        int hashCode1 = pojoNode1.hashCode();
        int hashCode2 = pojoNode2.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    /**
     * Test method serialize.
     */
    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        String testValue = "test";
        POJONode pojoNode = new POJONode(testValue);
        ObjectWriter objectWriter = objectMapper.getObjectWriter();
        StringWriter stringWriter = new StringWriter();
        objectWriter.writeValue(stringWriter, pojoNode);
        String expectedJson = "{\"value\":\"" + testValue + "\"}";
        String actualJson = stringWriter.toString();
        assertEquals(expectedJson, actualJson);
    }

    /**
     * Test method getPojo.
     */
    @Test
    public void testGetPojo() {
        String testValue = "test";
        POJONode pojoNode = new POJONode(testValue);
        assertEquals(testValue, pojoNode.getPojo());
    }

    /**
     * Test constructor with binary value.
     */
    @Test
    public void testPOJONodeWithBinaryValue() throws IOException {
        byte[] binaryValue = new byte[]{"test"};
        POJONode pojoNode = new POJONode(binaryValue);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("(binary value of 4 bytes)", pojoNode.toString());
        assertFalse(pojoNode.equals(new TextNode("test")));
        assertArrayEquals(binaryValue, (byte[]) pojoNode.getPojo());
    }

    private static class TestPojo {
        private String value;

        public TestPojo() {
        }

        public TestPojo(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TestPojo other = (TestPojo) o;
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return "TestPojo{" +
                    "value='" + value + '\'}';
        }
    }
}