package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONodeWithStringValue() throws JsonProcessingException {
 POJONode node = new POJONode("Hello World");
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals("Hello World", node.asText());

 // Test serialization
 String json = mapper.writeValueAsString(node);
 assertEquals("\"Hello World\"", json);
 }

 @Test
 public void testPOJONodeWithNumberValue() throws JsonProcessingException {
 POJONode node = new POJONode(123);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals("123", node.asText());

 // Test serialization
 String json = mapper.writeValueAsString(node);
 assertEquals("123", json);
 }

 @Test
 public void testPOJONodeWithBooleanValue() throws JsonProcessingException {
 POJONode node = new POJONode(true);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals("true", node.asText());

 // Test serialization
 String json = mapper.writeValueAsString(node);
 assertEquals("true", json);
 }

 @Test
 public void testPOJONodeWithNullValue() throws JsonProcessingException {
 POJONode node = new POJONode(null);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 assertEquals("null", node.asText());

 // Test serialization
 String json = mapper.writeValueAsString(node);
 assertEquals("null", json);
 }

 @Test(expected = IOException.class)
 public void testPOJONodeWithByteArrayValueThrowsIOException() throws IOException {
 POJONode node = new POJONode(new byte[] { 0x01, 0x02, 0x03 });
 fail("Should throw IOException when byte array is used");
 }

 @Test
 public void testPOJONodeWithCustomClassValue() throws JsonProcessingException {
 class CustomClass {
 int id;
 String name;

 public CustomClass(int id, String name) {
 this.id = id;
 this.name = name;
 }
 }

 CustomClass customObject = new CustomClass(1, "Test Object");
 POJONode node = new POJONode(customObject);
 assertEquals(JsonNodeType.POJO, node.getNodeType());

 // Test serialization
 String json = mapper.writeValueAsString(node);
 CustomClass deserializedObject = mapper.readValue(json, CustomClass.class);
 assertEquals(customObject.id, deserializedObject.id);
 assertEquals(customObject.name, deserializedObject.name);
 }

 @Test
 public void testPOJONodeEquality() {
 POJONode node1 = new POJONode("Hello World");
 POJONode node2 = new POJONode("Hello World");
 assertTrue(node1.equals(node2));
 }

 @Test
 public void testPOJONodeInequality() {
 POJONode node1 = new POJONode("Hello World");
 POJONode node2 = new POJONode("Hi there");
 assertFalse(node1.equals(node2));
 }

 @Test
 public void testPOJONodeHashCode() {
 POJONode node1 = new POJONode("Hello World");
 POJONode node2 = new POJONode("Hello World");
 assertEquals(node1.hashCode(), node2.hashCode());
 }

 @Test
 public void testPOJONodeToString() {
 POJONode node = new POJONode("Hello World");
 assertEquals("POJONode(value=Hello World)", node.toString());
 }
}