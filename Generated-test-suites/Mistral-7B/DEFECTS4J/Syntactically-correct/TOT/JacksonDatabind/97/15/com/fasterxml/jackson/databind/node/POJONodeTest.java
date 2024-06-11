package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.NodeSerializer;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class POJONodeTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.setDefaultTyping(DefaultTyping.OBJECT_WRAPPER);
 }

 @Test
 public void testPOJONodeSerialization() throws JsonProcessingException {
 Object pojo = newPojo(); // Assume this creates a valid POJO
 POJONode node = new POJONode(pojo);
 JsonNode expectedJson = mapper.valueToTree(pojo);

 // Test serialization to JSON
 JsonNode actualJson = mapper.treeToValue(node, JsonNode.class);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testPOJONodeNull() {
 POJONode node = new POJONode(null);
 // Test serialization to JSON
 // Expected to be null
 JsonNode actualJson = null;
 try {
 actualJson = mapper.treeToValue(node, JsonNode.class);
 } catch (IOException e) {
 // Ignore
 }
 Assert.assertNull(actualJson);
 }

 @Test
 public void testPOJONodeInvalidType() {
 POJONode node = new POJONode("invalid type");
 // Test serialization to JSON
 // Should throw exception
 Assert.assertThrows(RuntimeException.class, () -> mapper.treeToValue(node, JsonNode.class));
 }

 @Test
 public void testPOJONodeBinaryData() throws IOException {
 byte[] binaryData = new byte[] { 1, 2, 3 };
 POJONode node = new POJONode(binaryData);
 // Test serialization to JSON
 // Binary data should be serialized as base64 encoded string
 JsonNode expectedJson = mapper.valueToTree(binaryData);
 expectedJson.put("binary", NodeSerializer.instance.serializeToTree(binaryData));
 JsonNode actualJson = mapper.treeToValue(node, JsonNode.class);
 Assert.assertEquals(expectedJson, actualJson);
 }

 @Test
 public void testPOJONodeToString() {
 POJONode node = new POJONode(new String("test string"));
 String expectedString = "{\"binary\":null,\"text\":\"test string\"}";
 String actualString = node.toString();
 Assert.assertEquals(expectedString, actualString);
 }

 @Test
 public void testPOJONodeEquals() {
 Object pojo1 = newPojo();
 Object pojo2 = newPojo();
 POJONode node1 = new POJONode(pojo1);
 POJONode node2 = new POJONode(pojo2);

 // Test equals with different objects
 Assert.assertFalse(node1.equals(null));
 Assert.assertFalse(node1.equals("different object"));
 // Test equals with same object
 Assert.assertTrue(node1.equals(node1));
 // Test equals with identical nodes
 Assert.assertTrue(node1.equals(node1));
 // Test equals with identical nodes holding identical POJOs
 POJONode node3 = new POJONode(pojo1);
 Assert.assertTrue(node1.equals(node3));
 // Test equals with identical nodes holding different instances of identical POJOs
 Object pojo3 = new PojoCopyConstructor(pojo1); // Assume this creates a deep copy
 POJONode node4 = new POJONode(pojo3);
 Assert.assertTrue(node1.equals(node4));
 }

 @Test
 public void testPOJONodeHashCode() {
 Object pojo = newPojo();
 POJONode node1 = new POJONode(pojo);
 POJONode node2 = new POJONode(pojo);
 Assert.assertEquals(node1.hashCode(), node2.hashCode());
 }
}