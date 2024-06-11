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

 final ObjectMapper mapper = new ObjectMapper();
 final JsonFactory factory = mapper.getFactory();
 final JsonGenerator generator = factory.createGenerator(System.out);

 @Test
 public void testPOJONode_Object() throws Exception {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 assertNull(pojoNode.binaryValue());
 assertEquals("null", pojoNode.asText());
 assertFalse(pojoNode.asBoolean(false));
 assertSame(0, pojoNode.asInt(0));
 assertSame(0L, pojoNode.asLong(0L));
 assertSame(0.0D, pojoNode.asDouble(0.0D));

 // test serialization
 pojoNode.serialize(generator, new SerializerProvider() {
 @Override
 public void serializeValue(JsonGenerator g, Object value) throws IOException {
 fail("Should not call this method");
 }

 @Override
 public void defaultSerializeNull(JsonGenerator g) throws IOException {
 g.writeNull();
 }
 });
 assertTrue(pojoNode.toString().contains("null"));

 // test equals and hashcode
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 POJONode pojoNode3 = new POJONode("test");
 assertSame(pojoNode, pojoNode1);
 assertSame(pojoNode, pojoNode1);
 assertNotSame(pojoNode, pojoNode3);

 // test getPojo
 POJONode pojoNode4 = mapper.readTree("{\"test\":{\"value\":\"test\"}}").path("test");
 Object getPojo = pojoNode4.getPojo();
 assertEquals("test", getPojo.toString());
 }

 @Test
 public void testPOJONode_ByteArray() throws Exception {
 byte[] byteArray = new byte[] { 1, 2, 3 };
 POJONode pojoNode = new POJONode(byteArray);
 assertSame(JsonNodeType.BINARY, pojoNode.getNodeType());
 assertEquals(JsonToken.VALUE_BINARY_DATA, pojoNode.asToken());
 assertArrayEquals(byteArray, pojoNode.binaryValue());
 assertEquals("(binary value of 3 bytes)", pojoNode.asText());
 assertFalse(pojoNode.asBoolean(false));
 assertSame(0, pojoNode.asInt(0));
 assertSame(0L, pojoNode.asLong(0L));
 assertSame(0.0D, pojoNode.asDouble(0.0D));

 POJONode pojoNode1 = new POJONode(new byte[] { 1, 2, 3 });
 assertSame(pojoNode, pojoNode1);
 assertNotSame(pojoNode, new POJONode(new byte[] { 1, 2, 4 }));
 }

 @Test
 public void testPOJONode_RawValue() throws Exception {
 String rawValue = "{\"test\":\"test\"}";
 POJONode pojoNode = new POJONode(mapper.readTree(rawValue));
 assertSame(JsonNodeType.OBJECT, pojoNode.getNodeType());
 assertEquals(JsonToken.START_OBJECT, pojoNode.asToken());
 assertSame(rawValue, pojoNode.asText());
 assertFalse(pojoNode.asBoolean(false));
 assertSame(0, pojoNode.asInt(0));
 assertSame(0L, pojoNode.asLong(0L));
 assertSame(0.0D, pojoNode.asDouble(0.0D));
 // test serialization
 pojoNode.serialize(generator, new SerializerProvider() {
 @Override
 public void serializeValue(JsonGenerator g, Object value) throws IOException {
 fail("Should not call this method");
 }

 @Override
 public void defaultSerializeNull(JsonGenerator g) throws IOException {
 g.writeNull();
 }
 });
 assertTrue(pojoNode.toString().contains("test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPOJONode_InvalidArgument() throws Exception {
 new POJONode(null);
 }
}