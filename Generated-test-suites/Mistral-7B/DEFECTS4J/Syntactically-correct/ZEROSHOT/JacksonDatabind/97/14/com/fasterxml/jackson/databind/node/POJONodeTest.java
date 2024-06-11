package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 test file for POJONode class.
 */
public class POJONodeTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testNodeType() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode node = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
 }

 @Test
 public void testBinaryValue() {
 POJONode node = new POJONode(new byte[0]);
 assertArrayEquals(new byte[0], node.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode node1 = new POJONode(new String("Test"));
 assertEquals("Test", node1.asText());

 POJONode node2 = new POJONode(null);
 assertEquals("null", node2.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals("", node.asText("Default"));
 }

 @Test
 public void testAsBoolean() {
 POJONode nodeTrue = new POJONode(Boolean.TRUE);
 assertTrue(nodeTrue.asBoolean(Boolean.FALSE));

 POJONode nodeFalse = new POJONode(Boolean.FALSE);
 assertFalse(nodeFalse.asBoolean(Boolean.TRUE));

 POJONode nodeNull = new POJONode(null);
 assertFalse(nodeNull.asBoolean(Boolean.TRUE));
 }

 @Test
 public void testAsInt() {
 POJONode nodeInt1 = new POJONode(Integer.valueOf(1));
 assertEquals(1, nodeInt1.asInt(0));

 POJONode nodeInt2 = new POJONode(Integer.valueOf(-1));
 assertEquals(-1, nodeInt2.asInt(0));

 POJONode nodeInt3 = new POJONode(null);
 assertEquals(0, nodeInt3.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode nodeLong1 = new POJONode(Long.valueOf(1L));
 assertEquals(1L, nodeLong1.asLong(0L));

 POJONode nodeLong2 = new POJONode(Long.valueOf(-1L));
 assertEquals(-1L, nodeLong2.asLong(0L));

 POJONode nodeLong3 = new POJONode(null);
 assertEquals(0L, nodeLong3.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode nodeDouble1 = new POJONode(Double.valueOf(1.5));
 assertEquals(1.5, nodeDouble1.asDouble(0.0), 0.001);

 POJONode nodeDouble2 = new POJONode(Double.valueOf(-1.5));
 assertEquals(-1.5, nodeDouble2.asDouble(0.0), 0.001);

 POJONode nodeDouble3 = new POJONode(null);
 assertEquals(0.0, nodeDouble3.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws IOException {
 POJONode node = new POJONode(new Object());
 JsonGenerator gen = mapper.getFactory()
 .createGenerator(new StringWriter());
 SerializerProvider provider = mapper.getSerializerProvider();
 node.serialize(gen, provider);

 String json = gen.getOutputString();
 JsonNode actualNode = mapper.readTree(json);
 assertEquals(JsonNodeType.POJO, actualNode.getNodeType());
 }

 @Test
 public void testGetPojo() {
 POJONode node = new POJONode(new Object());
 assertEquals(node.getPojo().getClass(), Object.class);
 }

 @Test
 public void testEquals() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 assertTrue(node1.equals(node2));

 Object anotherObject = new Object();
 POJONode node3 = new POJONode(anotherObject);
 assertFalse(node1.equals(node3));
 }

 @Test
 public void testHashCode() {
 POJONode node1 = new POJONode(new Object());
 POJONode node2 = new POJONode(new Object());
 assertEquals(node1.hashCode(), node2.hashCode());
 }

 @Test
 public void testToString() {
 POJONode node1 = new POJONode("Test String");
 assertEquals("(value of Test String)", node1.toString());

 POJONode node2 = new POJONode(new Object());
 assertEquals("(value of [Ljava.lang.Object;@" + node2.hashCode() + ")", node2.toString());
 }
}