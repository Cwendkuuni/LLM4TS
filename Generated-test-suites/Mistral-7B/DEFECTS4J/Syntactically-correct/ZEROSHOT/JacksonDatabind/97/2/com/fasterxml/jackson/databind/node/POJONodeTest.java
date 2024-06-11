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
import com.fasterxml.jackson.databind.util.TestBase;

public class POJONodeTest extends TestBase {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testPOJONodeBasic() throws IOException {
 POJONode pojoNode = new POJONode(new String("Hello World"));
 JsonNode expected = mapper.valueToTree("Hello World");
 assertEquals(expected.getNodeType(), pojoNode.getNodeType());
 assertEquals(expected.asText(), pojoNode.asText());
 assertEquals(expected.asText(null), pojoNode.asText(null));
 assertEquals(expected.asInt(0), pojoNode.asInt(0));
 assertEquals(expected.asLong(0), pojoNode.asLong(0));
 assertEquals(expected.asDouble(0.0), pojoNode.asDouble(0.0));
 assertEquals(expected.asBoolean(false), pojoNode.asBoolean(false));
 }

 @Test
 public void testPOJONodeWithNull() throws IOException {
 POJONode pojoNode = new POJONode(null);
 JsonNode expected = mapper.valueToTree("null");
 assertEquals(expected.getNodeType(), pojoNode.getNodeType());
 assertEquals(expected.asText(), pojoNode.asText());
 assertEquals(expected.asText(null), pojoNode.asText(null));
 assertFalse(pojoNode.asBoolean(false));
 }

 @Test
 public void testPOJONodeWithBoolean() throws IOException {
 POJONode pojoNode = new POJONode(true);
 JsonNode expected = mapper.valueToTree(true);
 assertEquals(expected.getNodeType(), pojoNode.getNodeType());
 assertTrue(pojoNode.asBoolean(false));
 }

 @Test
 public void testPOJONodeWithNumber() throws IOException {
 POJONode pojoNode = new POJONode(123);
 JsonNode expected = mapper.valueToTree(123);
 assertEquals(expected.getNodeType(), pojoNode.getNodeType());
 assertEquals(expected.asInt(0), pojoNode.asInt(0));
 assertEquals(expected.asLong(0), pojoNode.asLong(0));
 assertEquals(expected.asDouble(0.0), pojoNode.asDouble(0.0));
 }

 @Test
 public void testPOJONodeWithSerializableObject() throws IOException, JsonProcessingException {
 TestClass testClass = new TestClass();
 testClass.setName("Test Name");
 POJONode pojoNode = new POJONode(testClass);
 JsonNode expected = mapper.valueToTree(testClass);
 assertEquals(expected.getNodeType(), pojoNode.getNodeType());
 assertEquals(expected.get("name").asText(), pojoNode.getPojo().getName());

 // Test serialization
 JsonGenerator gen = createMySerializer(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false).getGenerator();
 POJONode serializedNode = mapper.readValue(mapper.writeValueAsString(pojoNode), POJONode.class);
 pojoNode.serialize(gen, new SerializerProvider());
 assertEquals(pojoNode.asText(), serializedNode.asText());
 }

 @Test
 public void testPOJONodeWithSubClass() throws IOException {
 TestSubClass testSubClass = new TestSubClass();
 testSubClass.setName("Test Name");
 POJONode pojoNode = new POJONode(testSubClass);
 JsonNode expected = mapper.valueToTree(testSubClass);
 assertEquals(expected.getNodeType(), pojoNode.getNodeType());
 assertEquals(expected.get("name").asText(), pojoNode.getPojo().getName());
 }

 @Test
 public void testPOJONodeWithArray() throws IOException {
 POJONode[] pojoNodes = new POJONode[] { new POJONode(new int[] { 1, 2, 3 }), new POJONode(new double[] { 1.0, 2.0, 3.0 }) };
 JsonNode expected = mapper.valueToTree(new Object[] { new int[] { 1, 2, 3 }, new double[] { 1.0, 2.0, 3.0 } });
 JsonNode actual = mapper.valueToTree(pojoNodes);
 assertEquals(expected.getNodeType(), actual.getNodeType());
 assertArrayEquals(expected.get(0).asInts(), actual.get(0).asInts());
 assertArrayEquals(expected.get(1).asInts(), actual.get(1).asInts());
 }

 private static class TestClass implements JsonSerializable {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 @Override
 public void serialize(JsonGenerator gen, SerializerProvider ctxt) throws IOException {
 gen.writeFieldName("name");
 gen.writeString(name);
 }
 }

 private static class TestSubClass extends TestClass {
 // Empty
 }
}