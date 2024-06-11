package com.fasterxml.jackson.databind.node;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest {

 // Test constructor with JsonNode
 @Test
 public void testTreeTraversingParserWithJsonNode() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("[]");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 }

 // Test constructor with JsonNode and ObjectCodec
 @Test
 public void testTreeTraversingParserWithJsonNodeAndObjectCodec() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 JsonNode jsonNode = objectMapper.readTree("{'key': 'value'}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectMapper.getFactory());
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 }

 // Test nextToken() with empty JsonNode
 @Test
 public void testTreeTraversingParserNextTokenWithEmptyJsonNode() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("{}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertNull(token);
 }

 // Test nextToken() with JsonNode containing array
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeArray() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3]");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 }

 // Test nextToken() with JsonNode containing object
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeObject() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("{'name': 'John'}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 }

 // Test nextToken() with JsonNode containing value
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeValue() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("42");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 }

 // Test nextToken() with JsonNode containing null value
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeNullValue() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("null");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token); // or END_ARRAY for null in array
 }

 // Test nextToken() with JsonNode containing string value
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeStringValue() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("\"Hello World\"");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 }

 // Test nextToken() with JsonNode containing boolean value
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeBooleanValue() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree(true.toString());
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_Boolean, token);
 }

 // Test nextToken() with JsonNode containing number value
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeNumberValue() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("3.14");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 }

 // Test nextToken() with JsonNode containing array with nested object
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeNestedObject() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("{" +
 "\"name\": {" +
 "\"first\": \"John\"," +
 "\"last\": \"Doe\"" +
 "}," +
 "\"age\": 30" +
 "}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 }

 // Test nextToken() with JsonNode containing array with nested array
 @Test
 public void testTreeTraversingParserNextTokenWithJsonNodeNestedArray() throws Exception {
 JsonNode jsonNode = new ObjectMapper().readTree("[1, [2, 3], 4]");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 }

 // Test isClosed() with closed parser
 @Test
 public void testTreeTraversingParserIsClosedWithClosedParser() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 parser.close();
 assertTrue(parser.isClosed());
 }

 // Test isClosed() with open parser
 @Test
 public void testTreeTraversingParserIsClosedWithOpenParser() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree("{}"));
 assertFalse(parser.isClosed());
 }

 // Test nextToken() with closed parser
 @Test(expected = JsonParseException.class)
 public void testTreeTraversingParserNextTokenWithClosedParser() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(new ObjectMapper().readTree("{}"));
 parser.close();
 parser.nextToken();
 }
}