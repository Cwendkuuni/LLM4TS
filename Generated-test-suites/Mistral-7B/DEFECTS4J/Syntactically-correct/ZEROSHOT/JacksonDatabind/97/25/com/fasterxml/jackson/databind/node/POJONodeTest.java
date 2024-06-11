package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class POJONodeTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testNodeType() throws JsonProcessingException {
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
 assertArrayEquals(new byte[0], node.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode node = new POJONode("test");
 assertEquals("test", node.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode node = new POJONode(null);
 assertEquals("null", node.asText("defaultValue"));
 }

 @Test
 public void testAsBoolean() {
 POJONode nodeTrue = new POJONode(Boolean.TRUE);
 assertTrue(nodeTrue.asBoolean(false));

 POJONode nodeFalse = new POJONode(Boolean.FALSE);
 assertFalse(nodeFalse.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode nodeInt = new POJONode(Integer.valueOf(42));
 assertEquals(42, nodeInt.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode nodeLong = new POJONode(Long.valueOf(42L));
 assertEquals(42L, nodeLong.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode nodeDouble = new POJONode(Double.valueOf(42.0));
 assertEquals(42.0, nodeDouble.asDouble(0.0), 0.0);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 POJONode node = new POJONode("test");
 StringWriter sw = new StringWriter();
 JsonGenerator gen = mapper.getFactory().createGenerator(sw);
 node.serialize(gen, null);
 assertEquals("test", gen.getOutputValue());
 }

 @Test
 public void testGetPojo() {
 POJONode node = new POJONode(new Object());
 assertEquals(node.getPojo().getClass(), Object.class);
 }

 @Test
 public void testEquals() {
 POJONode node1 = new POJONode("test1");
 POJONode node2 = new POJONode("test2");
 POJONode node3 = new POJONode("test1");

 assertEquals(node1, node1);
 assertNotEquals(node1, node2);
 assertEquals(node1, node3);
 }

 @Test
 public void testHashCode() {
 POJONode node1 = new POJONode("test1");
 POJONode node2 = new POJONode("test2");
 POJONode node3 = new POJONode("test1");

 int h1 = node1.hashCode();
 int h2 = node2.hashCode();
 int h3 = node3.hashCode();

 assertEquals(h1, h3);
 assertNotEquals(h1, h2);
 }

 @Test
 public void testToString() {
 POJONode node = new POJONode(new Object());
 assertEquals("POJONode{_value= com.fasterxml.jackson.databind.node.POJONode@7852e9}", node.toString());
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 POJONode node = new POJONode(new Object());
 StdSerializer<POJONode> serializer = mapper.findValueSerializer(POJONode.class, null);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(node, gen, null);
 }
}