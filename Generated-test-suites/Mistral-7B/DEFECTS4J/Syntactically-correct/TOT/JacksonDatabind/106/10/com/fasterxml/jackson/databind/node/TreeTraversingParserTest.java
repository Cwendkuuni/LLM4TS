package com.fasterxml.jackson.databind.node; // import statements go here

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private JsonNode node;

 @Before
 public void setUp() {
 // Create a sample JSON node for testing
 JsonFactory factory = new JsonFactory();
 node = factory.createObjectNode();
 node.put("stringKey", "hello world");
 node.putNumber("intKey", 123);
 node.putNumber("floatKey", 3.14);
 node.putBoolean("boolKey", true);
 node.putNull("nullKey");
 node.putArray("arrayKey")
 .add(JsonNodeFactory.valueNode("first"));
 node.put("nestedObjectKey", factory.createObjectNode()
 .put("grandChild", JsonNodeFactory.valueNode(42)));
 }

 @Test
 public void testParseSimpleObject() throws IOException {
 parser = new TreeTraversingParser(node);
 assertTrue(parser.nextToken().isStartObject());
 parser.nextFieldName(); // "stringKey"
 assertEquals("hello world", parser.getText());
 parser.nextToken(); // "intKey"
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getClass());
 assertEquals(123, parser.getIntValue());
 parser.nextToken(); // "floatKey"
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getClass());
 assertEquals(3.14, parser.getDoubleValue(), 0.001);
 parser.nextToken(); // "boolKey"
 assertEquals(JsonToken.VALUE_Boolean, parser.nextToken().getClass());
 assertTrue(parser.getBooleanValue());
 parser.nextToken(); // "nullKey"
 assertNull(parser.nextValue());
 parser.nextToken(); // "nestedObjectKey"
 assertTrue(parser.nextToken().isStartObject());
 // test nested object
 parser.nextFieldName(); // "grandChild"
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getClass());
 assertEquals(42, parser.getIntValue());
 parser.nextToken(); // end of nested object
 parser.nextToken(); // end of root object
 parser.close();
 }

 @Test
 public void testParseEmptyArray() throws IOException {
 parser = new TreeTraversingParser(node.get("arrayKey"));
 assertTrue(parser.nextToken().isStartArray());
 parser.nextToken(); // empty array
 parser.nextToken(); // end of array
 parser.close();
 }

 @Test
 public void testParseEmptyObject() throws IOException {
 parser = new TreeTraversingParser(node.get("nullKey"));
 assertTrue(parser.nextToken().isNull());
 parser.close();
 }

 @Test(expected = IOException.class)
 public void testParseNonJsonNode() {
 parser = new TreeTraversingParser("not a json");
 parser.nextToken(); // this will throw an IOException
 }

 @Test(expected = JsonParseException.class)
 public void testParseInvalidJson() throws IOException {
 JsonFactory factory = new JsonFactory();
 node = factory.createTextNode("{foo:bar}"); // missing colon
 parser = new TreeTraversingParser(node);
 parser.nextToken(); // "START_OBJECT"
 parser.nextFieldName(); // "foo"
 parser.nextToken(); // "START_STRING"
 parser.nextTextCharacters(); // read some characters
 parser.nextToken(); // this will throw a JsonParseException
 parser.close();
 }
}