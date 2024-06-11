package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class POJONodeTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testPOJONode_NullValue() throws JsonProcessingException {
 POJONode node = new POJONode(null);
 assertTrue(node.asBoolean(false));
 assertEquals("null", node.asText());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 mapper.writeValueAsString(node); // should not throw IOException
 }

 @Test
 public void testPOJONode_StringValue() throws JsonProcessingException {
 POJONode node = new POJONode("Hello World");
 assertFalse(node.asBoolean(false));
 assertEquals("Hello World", node.asText());
 assertEquals("Hello World", node.getPojo().toString());
 mapper.writeValueAsString(node);
 }

 @Test
 public void testPOJONode_BooleanValue() throws JsonProcessingException {
 POJONode node = new POJONode(true);
 assertTrue(node.asBoolean(false));
 mapper.writeValueAsString(node);
 }

 @Test
 public void testPOJONode_NumberValue() throws JsonProcessingException {
 POJONode node = new POJONode(123);
 assertEquals(123, node.asInt(0));
 mapper.writeValueAsString(node);
 }

 @Test
 public void testPOJONode_POJOValue() throws JsonProcessingException {
 Foo foo = new Foo();
 foo.setName("Test");
 POJONode node = new POJONode(foo);
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 mapper.writeValueAsString(node);
 }

 @Test
 public void testPOJONode_BinaryValue() throws JsonProcessingException {
 byte[] data = "Test Data".getBytes();
 POJONode node = new POJONode(data);
 assertEquals(data.length, node.binaryValue().length);
 mapper.writeValueAsString(node);
 }

 @Test
 public void testPOJONode_Equals() {
 POJONode node1 = new POJONode(1);
 POJONode node2 = new POJONode(1);
 assertTrue(node1.equals(node2));
 POJONode node3 = new POJONode("Test");
 assertFalse(node1.equals(node3));
 }

 @Test
 public void testPOJONode_HashCode() {
 POJONode node1 = new POJONode(1);
 POJONode node2 = new POJONode(1);
 assertEquals(node1.hashCode(), node2.hashCode());
 POJONode node3 = new POJONode("Test");
 assertNotEquals(node1.hashCode(), node3.hashCode());
 }

 @Test
 public void testPOJONode_Serialize() throws JsonProcessingException {
 POJONode node = new POJONode("Hello World");
 String json = mapper.writeValueAsString(node);
 assertEquals("{\"@type\":\"POJO\",\"value\":\"Hello World\"}", json);
 }
}

 class Foo {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
}