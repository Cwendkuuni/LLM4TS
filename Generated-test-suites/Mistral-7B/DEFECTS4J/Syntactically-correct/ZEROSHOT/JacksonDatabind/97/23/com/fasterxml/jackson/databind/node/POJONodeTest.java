package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.util.TestObjectMapper;

import org.junit.Test;

/**
 * JUnit 4 test file for POJONode class.
 */
public class POJONodeTest {

    private ObjectMapper mapper = TestObjectMapper.getDefaultInstance();

    /**
     * Test constructor with valid POJO.
     */
    @Test
    public void testConstructorWithValidPojo() {
        POJONode pojoNode = new POJONode(new TestClass());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    /**
     * Test getting POJO.
     */
    @Test
    public void testGetPojo() {
        TestClass testClass = new TestClass();
        POJONode pojoNode = new POJONode(testClass);
        assertEquals(testClass, pojoNode.getPojo());
    }

    /**
     * Test base class overrides: getNodeType().
     */
    @Test
    public void testGetNodeType() {
        TestClass testClass = new TestClass();
        POJONode pojoNode = new POJONode(testClass);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    /**
     * Test base class override: asText().
     */
    @Test
    public void testAsText() throws JsonProcessingException {
        TestClass testClass = new TestClass();
        POJONode pojoNode = mapper.valueToTree(testClass);
        assertEquals("{\"name\":\"TestClass\",\"age\":30}", pojoNode.asText());
    }

    /**
     * Test base class override: asText(String defaultValue).
     */
    @Test
    public void testAsTextWithDefaultValue() throws JsonProcessingException {
        TestClass testClass = new TestClass();
        POJONode pojoNode = mapper.valueToTree(testClass);
        assertEquals("null", pojoNode.asText((String) null));
    }

    /**
     * Test base class override: asBoolean(boolean defaultValue).
     */
    @Test
    public void testAsBoolean() {
        TestClass testClass = new TestClass(true);
        POJONode pojoNode = new POJONode(testClass);
        assertEquals(true, pojoNode.asBoolean(false));
    }

    /**
     * Test base class override: asInt(int defaultValue).
     */
    @Test
    public void testAsInt() {
        TestClass testClass = new TestClass(30);
        POJONode pojoNode = new POJONode(testClass);
        assertEquals(30, pojoNode.asInt(0));
    }

    /**
     * Test base class override: asLong(long defaultValue).
     */
    @Test
    public void testAsLong() {
        TestClass testClass = new TestClass(30L);
        POJONode pojoNode = new POJONode(testClass);
        assertEquals(30L, pojoNode.asLong(0L));
    }

    /**
     * Test base class override: asDouble(double defaultValue).
     */
    @Test
    public void testAsDouble() {
        TestClass testClass = new TestClass(30.5);
        POJONode pojoNode = new POJONode(testClass);
        assertEquals(30.5, pojoNode.asDouble(0.0), 0.001);
    }

    /**
     * Test serialization.
     */
    @Test
    public void testSerialization() throws JsonProcessingException {
        TestClass testClass = new TestClass();
        POJONode pojoNode = mapper.valueToTree(testClass);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        pojoNode.serialize(gen, new SerializerProvider());

        String expectedJson = "{\"name\":\"TestClass\",\"age\":30}";
        String actualJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojoNode);
        assertEquals(expectedJson, actualJson);
    }

    /**
     * Test equals().
     */
    @Test
    public void testEquals() {
        TestClass testClass1 = new TestClass();
        TestClass testClass2 = new TestClass();
        POJONode pojoNode1 = new POJONode(testClass1);
        POJONode pojoNode2 = new POJONode(testClass2);

        assertFalse(pojoNode1.equals(pojoNode2));

        TestClass testClass3 = new TestClass(testClass1);
        POJONode pojoNode3 = new POJONode(testClass3);
        assertTrue(pojoNode1.equals(pojoNode3));
    }

    /**
     * Test hashCode().
     */
    @Test
    public void testHashCode() {
        TestClass testClass = new TestClass();
        POJONode pojoNode1 = new POJONode(testClass);
        POJONode pojoNode2 = new POJONode(testClass);

        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    /**
     * Test toString().
     */
    @Test
    public void testToString() throws JsonProcessingException {
        TestClass testClass = new TestClass();
        POJONode pojoNode = mapper.valueToTree(testClass);
        assertEquals("{\"name\":\"TestClass\",\"age\":30}", pojoNode.toString());
    }
}

class TestClass implements JsonSerializable {
    private final boolean booleanValue;
    private final int intValue;
    private final long longValue;
    private final double doubleValue;
    private final String stringValue;

    public TestClass() {
        this(false, 30, 30L, 30.5, "Hello World!");
    }

    public TestClass(boolean booleanValue) {
        this.booleanValue = booleanValue;
        this.intValue = 30;
        this.longValue = 30L;
        this.doubleValue = 30.5;
        this.stringValue = null;
    }

    public TestClass(int intValue) {
        this.booleanValue = false;
        this.intValue = intValue;
        this.longValue = 30L;
        this.doubleValue = 30.5;
        this.stringValue = null;
    }

    public TestClass(long longValue) {
        this.booleanValue = false;
        this.intValue = 30;
        this.longValue = longValue;
        this.doubleValue = 30.5;
        this.stringValue = null;
    }

    public TestClass(double doubleValue) {
        this.booleanValue = false;
        this.intValue = 30;
        this.longValue = 30L;
        this.doubleValue = doubleValue;
        this.stringValue = null;
    }

    public TestClass(String stringValue) {
        this.booleanValue = false;
        this.intValue = 30;
        this.longValue = 30L;
        this.doubleValue = 30.5;
        this.stringValue = stringValue;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(this);
    }
}