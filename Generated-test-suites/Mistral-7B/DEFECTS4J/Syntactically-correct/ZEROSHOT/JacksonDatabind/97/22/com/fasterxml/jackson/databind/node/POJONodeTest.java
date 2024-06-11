package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

/**
 * Comprehensive JUnit 4 test file for POJONode class.
 */
public class POJONodeTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONode() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 assertNull(pojoNode.binaryValue());

 assertEquals("null", pojoNode.asText());
 assertEquals("defaultValue", pojoNode.asText("defaultValue"));
 assertFalse(pojoNode.asBoolean(false));
 assertFalse(pojoNode.asBoolean(true));
 assertSame(0, pojoNode.asInt(0));
 assertSame(1, pojoNode.asInt(1));
 assertSame(1.0d, pojoNode.asDouble(1.0d));
 assertSame(1L, pojoNode.asLong(1L));

 POJONode pojoNodeWithBoolean = new POJONode(true);
 assertTrue(pojoNodeWithBoolean.asBoolean(false));
 assertTrue(pojoNodeWithBoolean.asBoolean(true));

 POJONode pojoNodeWithNumber = new POJONode(1);
 assertEquals(1, pojoNodeWithNumber.asInt(0));
 assertEquals(1.0d, pojoNodeWithNumber.asDouble(0.0d));
 assertEquals(1L, pojoNodeWithNumber.asLong(0L));

 POJONode pojoNodeWithLong = new POJONode(1L);
 assertEquals(1L, pojoNodeWithLong.asLong(0L));

 POJONode pojoNodeWithDouble = new POJONode(1.0d);
 assertEquals(1.0d, pojoNodeWithDouble.asDouble(0.0d));

 POJONode pojoNodeWithString = new POJONode("Hello");
 assertEquals("Hello", pojoNodeWithString.asText());

 POJONode pojoNodeWithJsonSerializable = new POJONode(new MyClass());
 try {
 pojoNodeWithJsonSerializable.serialize(mapper.getSerializerProvider().getRootValueSerializer(), mapper.getSerializerProvider());
 } catch (JsonProcessingException e) {
 fail("Expected serialization to not throw an exception");
 }

 POJONode pojoNodeWithNull = new POJONode(null);
 assertNull(pojoNodeWithNull.asText());
 assertFalse(pojoNodeWithNull.asBoolean(false));
 assertFalse(pojoNodeWithNull.asBoolean(true));
 assertSame(0, pojoNodeWithNull.asInt(0));
 assertSame(0, pojoNodeWithNull.asLong(0L));
 assertSame(0.0d, pojoNodeWithNull.asDouble(0.0d));

 POJONode pojoNodeWithArray = new POJONode(new int[] { 1, 2, 3 });
 fail("Expected exception: Cannot convert value of type [I to POJONode");
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 POJONode pojoNode3 = new POJONode("Hello");

 assertTrue(pojoNode1.equals(pojoNode1));
 assertTrue(pojoNode1.equals(pojoNode2));
 assertFalse(pojoNode1.equals(pojoNode3));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 POJONode pojoNode3 = new POJONode("Hello");

 int hashCode1 = pojoNode1.hashCode();
 int hashCode2 = pojoNode2.hashCode();
 int hashCode3 = pojoNode3.hashCode();

 assertTrue(pojoNode1.hashCode() == hashCode1);
 assertTrue(pojoNode2.hashCode() == hashCode2);
 assertNotSame(pojoNode1.hashCode(), hashCode3);
 }

 @Test
 public void testToString() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals("(POJONode: null)", pojoNode.toString());

 POJONode pojoNodeWithBoolean = new POJONode(true);
 assertEquals("(POJONode: true)", pojoNodeWithBoolean.toString());

 POJONode pojoNodeWithNumber = new POJONode(1);
 assertEquals("(POJONode: 1)", pojoNodeWithNumber.toString());

 POJONode pojoNodeWithLong = new POJONode(1L);
 assertEquals("(POJONode: 1)", pojoNodeWithLong.toString());

 POJONode pojoNodeWithDouble = new POJONode(1.0d);
 assertEquals("(POJONode: 1.0)", pojoNodeWithDouble.toString());

 POJONode pojoNodeWithString = new POJONode("Hello");
 assertEquals("(POJONode: Hello)", pojoNodeWithString.toString());
 }
}

 class MyClass {
 // Empty class for testing POJONode serialization
 }