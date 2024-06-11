package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testgetNodeType() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
 }

 @Test
 public void testAsToken() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
 }

 @Test
 public void testBinaryValue() throws IOException {
 POJONode pojoNode = new POJONode(new byte[0]);
 assertArrayEquals(new byte[0], pojoNode.binaryValue());
 }

 @Test
 public void testAsText() {
 POJONode pojoNode = new POJONode(new String("hello"));
 assertEquals("hello", pojoNode.asText());
 POJONode nullPojoNode = new POJONode(null);
 assertEquals("null", nullPojoNode.asText());
 }

 @Test
 public void testAsTextWithDefaultValue() {
 POJONode pojoNode = new POJONode("hello");
 assertEquals("hello", pojoNode.asText());
 POJONode nullPojoNode = new POJONode(null);
 assertEquals("null", nullPojoNode.asText());
 POJONode emptyPojoNode = new POJONode(new Object());
 assertEquals("", emptyPojoNode.asText("default Value"));
 }

 @Test
 public void testAsBoolean() {
 POJONode pojoNodeBoolTrue = new POJONode(true);
 assertTrue(pojoNodeBoolTrue.asBoolean(false));
 POJONode pojoNodeBoolFalse = new POJONode(false);
 assertFalse(pojoNodeBoolFalse.asBoolean(true));
 POJONode pojoNodeNull = new POJONode(null);
 assertFalse(pojoNodeNull.asBoolean(true));
 }

 @Test
 public void testAsInt() {
 POJONode pojoNodeIntOne = new POJONode(1);
 assertEquals(1, pojoNodeIntOne.asInt(0));
 POJONode pojoNodeIntMinusOne = new POJONode(-1);
 assertEquals(-1, pojoNodeIntMinusOne.asInt(0));
 POJONode pojoNodeNull = new POJONode(null);
 assertEquals(0, pojoNodeNull.asInt(0));
 }

 @Test
 public void testAsLong() {
 POJONode pojoNodeLongOne = new POJONode(1L);
 assertEquals(1L, pojoNodeLongOne.asLong(0L));
 POJONode pojoNodeLongMinusOne = new POJONode(-1L);
 assertEquals(-1L, pojoNodeLongMinusOne.asLong(0L));
 POJONode pojoNodeNull = new POJONode(null);
 assertEquals(0L, pojoNodeNull.asLong(0L));
 }

 @Test
 public void testAsDouble() {
 POJONode pojoNodeDoubleOne = new POJONode(1.0);
 assertEquals(1.0, pojoNodeDoubleOne.asDouble(0.0), 0.001);
 POJONode pojoNodeDoubleMinusOne = new POJONode(-1.0);
 assertEquals(-1.0, pojoNodeDoubleMinusOne.asDouble(0.0), 0.001);
 POJONode pojoNodeNull = new POJONode(null);
 assertEquals(0.0, pojoNodeNull.asDouble(0.0), 0.001);
 }

 @Test
 public void testSerialize() throws JsonGenerationException, IOException {
 JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 POJONode pojoNode = new POJONode("hello");
 pojoNode.serialize(generator, new SerializerProvider() {
 @Override public void
 setSerializerInfo(JsonSerializerInfo info, SerializerProvider provider) {}

 @Override public void
 resetBoundValue() {}

 @Override public void
 flush() throws IOException {}

 @Override public void
 close() throws IOException {}
 });
 assertEquals("\"hello\"", generator.getOutputBuffer().toString());
 generator.close();
 }

 @Test
 public void testGetPojo() {
 POJONode pojoNode = new POJONode(new Object());
 assertEquals(pojoNode.getPojo(), pojoNode._value);
 }

 @Test
 public void testEquals() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertTrue(pojoNode1.equals(pojoNode2));
 POJONode pojoNode3 = new POJONode(new Object());
 POJONode pojoNode4 = new POJONode("not equal");
 assertFalse(pojoNode1.equals(pojoNode3));
 assertFalse(pojoNode1.equals(pojoNode4));
 }

 @Test
 public void testHashCode() {
 POJONode pojoNode1 = new POJONode(new Object());
 POJONode pojoNode2 = new POJONode(new Object());
 assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
 POJONode pojoNode3 = new POJONode("not equal");
 assertNotEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
 }

 @Test
 public void testToString() {
 POJONode pojoNode1 = new POJONode(new Object());
 assertEquals("POJONode(wrappedObject=com.fasterxml.jackson.databind.node.POJONode@15db9782)", pojoNode1.toString());
 POJONode pojoNode2 = new POJONode(new String("hello"));
 assertEquals("POJONode(wrappedObject=java.lang.String@7852e9a9, text=hello)", pojoNode2.toString());
 }

 @Test
 public void testParse() throws JsonMappingException, IOException {
 JsonParser parser = objectMapper.getFactory().createParser(new StringBuilder("#root: [{\"_class\":\"com.fasterxml.jackson.databind.node.POJONode\", \"_value\":{\"name\":\"test\"}, \"name\":\"test\"}]").toString());
 JsonNode rootNode = parser.readValueAsTree();
 JsonNode pojoNode = rootNode.get(0);
 assertInstanceOf(POJONode.class, pojoNode);
 assertEquals("test", pojoNode.getPojo().getClass().getDeclaredField("name").get(pojoNode.getPojo()));
 }
}