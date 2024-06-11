package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

public class POJONodeTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testPOJONodeWithNullValue() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(null);
 Object pojo = pojoNode.getPojo();
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertNull(pojo);
 }

 @Test
 public void testPOJONodeWithSimpleValue() throws JsonProcessingException {
 String testValue = "Hello World";
 POJONode pojoNode = new POJONode(testValue);
 Object pojo = pojoNode.getPojo();
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertEquals(testValue, pojo.toString());
 }

 @Test
 public void testPOJONodeWithNumberValue() throws JsonProcessingException {
 int testIntValue = 123;
 POJONode pojoNode = new POJONode(testIntValue);
 Object pojo = pojoNode.getPojo();
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertEquals(testIntValue, pojo.hashCode());
 }

 @Test
 public void testPOJONodeWithBooleanValue() throws JsonProcessingException {
 boolean testBooleanValue = true;
 POJONode pojoNode = new POJONode(testBooleanValue);
 Object pojo = pojoNode.getPojo();
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertEquals(testBooleanValue, pojo.booleanValue());
 }

 @Test
 public void testPOJONodeWithCustomSerializableValue() throws JsonProcessingException {
 class TestClass implements JsonSerializable {
 private int id;

 public TestClass() {}

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeNumber(id);
 }

 public int getId() {
 return id;
 }

 public void setId(int id) {
 this.id = id;
 }
 }

 TestClass testClass = new TestClass();
 testClass.setId(123);
 POJONode pojoNode = new POJONode(testClass);
 Object pojo = pojoNode.getPojo();
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertEquals(testClass.getClass(), pojo.getClass());
 }

 @Test(expected = IOException.class)
 public void testPOJONodeWithByteArrayValueThrowsIOException() throws JsonProcessingException {
 byte[] testByteArrayValue = {1, 2, 3};
 POJONode pojoNode = new POJONode(testByteArrayValue);
 // This method should throw IOException when invoked on POJONode
 pojoNode.binaryValue();
 }

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 POJONode pojoNode = new POJONode(new String("Hello World"));
 ObjectMapper objectMapper = new ObjectMapper();
 String json = objectMapper.writeValueAsString(pojoNode);
 assertEquals("{\"@type\":\"POJO\",\"value\":{\"@class\":\"java.lang.String\",\"@value\":\"Hello World\"}}", json);
 }
}