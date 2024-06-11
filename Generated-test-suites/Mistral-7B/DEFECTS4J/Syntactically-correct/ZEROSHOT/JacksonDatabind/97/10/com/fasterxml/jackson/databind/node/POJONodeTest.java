package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer.StandardDeserializer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.*;

/**
 * Test class for POJONode
 */
public class POJONodeTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @BeforeClass
 public static void setup() throws Exception {
 objectMapper.registerModule(new SimpleModule()
 .addDeserializer(String.class, new StringDeserializer(null)));
 }

 @Test
 public void testPOJONodeGetNodeType() {
 POJONode node = new POJONode("test");
 assertEquals(JsonNodeType.POJO, node.getNodeType());
 }

 @Test
 public void testPOJONodeAsToken() {
 POJONode node = new POJONode("test");
 assertEquals(JsonToken.VALUE_STRING, node.asToken());
 }

 @Test
 public void testPOJONodeBinaryValue() throws IOException {
 byte[] byteArray = new byte[] { 1, 2, 3 };
 POJONode nodeWithByte = new POJONode(byteArray);
 byte[] result = nodeWithByte.binaryValue();
 assertArrayEquals(byteArray, result);

 POJONode nodeWithoutByte = new POJONode("test");
 byte[] resultWithoutByte = nodeWithoutByte.binaryValue();
 assertNull(resultWithoutByte);
 }

 @Test
 public void testPOJONodeAsText() {
 POJONode nodeWithString = new POJONode("test");
 assertEquals("test", nodeWithString.asText());

 POJONode nodeWithNull = new POJONode(null);
 assertEquals("null", nodeWithNull.asText());
 }

 @Test
 public void testPOJONodeAsTextWithDefaultValue() {
 POJONode nodeWithNull = new POJONode(null);
 assertEquals("null", nodeWithNull.asText("default"));

 POJONode nodeWithString = new POJONode("test");
 assertEquals("test", nodeWithString.asText("default"));
 }

 @Test
 public void testPOJONodeAsBoolean() {
 POJONode nodeWithBooleanTrue = new POJONode(true);
 assertTrue(nodeWithBooleanTrue.asBoolean(false));

 POJONode nodeWithBooleanFalse = new POJONode(false);
 assertFalse(nodeWithBooleanFalse.asBoolean(true));

 POJONode nodeWithNull = new POJONode(null);
 assertEquals(false, nodeWithNull.asBoolean(true));
 }

 @Test
 public void testPOJONodeAsInt() {
 POJONode nodeWithInt = new POJONode(1);
 assertEquals(1, nodeWithInt.asInt(0));

 POJONode nodeWithNull = new POJONode(null);
 assertEquals(0, nodeWithNull.asInt(0));
 }

 @Test
 public void testPOJONodeAsLong() {
 POJONode nodeWithLong = new POJONode(1L);
 assertEquals(1L, nodeWithLong.asLong(0L));

 POJONode nodeWithNull = new POJONode(null);
 assertEquals(0L, nodeWithNull.asLong(0L));
 }

 @Test
 public void testPOJONodeAsDouble() {
 POJONode nodeWithDouble = new POJONode(1.0);
 assertEquals(1.0, nodeWithDouble.asDouble(0.0), 0.001);

 POJONode nodeWithNull = new POJONode(null);
 assertEquals(0.0, nodeWithNull.asDouble(0.0), 0.001);
 }

 @Test
 public void testPOJONodeSerialize() throws IOException {
 POJONode node = new POJONode("test");
 StringWriter stringWriter = new StringWriter();
 JsonGenerator jsonGenerator = objectMapper.getFactory()
 .createGenerator(stringWriter);
 node.serialize(jsonGenerator, objectMapper.getSerializerProvider());
 assertEquals("\"test\"", stringWriter.toString());

 ObjectWrapper ow = new ObjectMapper()
 .valueToTree(new Object());
 POJONode nodeWithPojo = new POJONode(ow);
 StringWriter stringWriter2 = new StringWriter();
 JsonGenerator jsonGenerator2 = objectMapper.getFactory()
 .createGenerator(stringWriter2);
 nodeWithPojo.serialize(jsonGenerator2, objectMapper.getSerializerProvider());
 assertEquals("{}", stringWriter2.toString());
 }

 @Test
 public void testPOJONodeGetPojo() {
 String testString = "test";
 POJONode nodeWithString = new POJONode(testString);
 assertEquals(testString, nodeWithString.getPojo());
}

 @Test
 public void testPOJONodeEquals() {
 POJONode node1 = new POJONode("test1");
 POJONode node2 = new POJONode("test1");
 assertTrue(node1.equals(node2));

 POJONode node3 = new POJONode("test2");
 assertFalse(node1.equals(node3));

 POJONode node4 = new POJONode(1);
 assertFalse(node1.equals(node4));
 }

 @Test
 public void testPOJONodeHashCode() {
 POJONode node1 = new POJONode("test1");
 POJONode node2 = new POJONode("test1");
 assertEquals(node1.hashCode(), node2.hashCode());

 POJONode node3 = new POJONode("test2");
 int hashCode1 = node1.hashCode();
 int hashCode2 = node3.hashCode();
 assertNotSame(hashCode1, hashCode2);
 }

 @Test
 public void testPOJONodeToString() {
 POJONode nodeWithString = new POJONode("test");
 assertEquals("POJONode(test)", nodeWithString.toString());

 POJONode nodeWithBinary = new POJONode(new byte[] { 1, 2, 3 });
 assertEquals("POJONode(binary value of 3 bytes)", nodeWithBinary.toString());

 POJONode nodeWithRawValue = new POJONode(new RawValue("test"));
 assertEquals("POJONode(raw value 'test')", nodeWithRawValue.toString());

 POJONode nodeWithPojo = new POJONode(new Object());
 assertEquals("POJONode(...)", nodeWithPojo.toString());
 }
}