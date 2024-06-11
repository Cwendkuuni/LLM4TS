package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private JsonNode testNode;

 @Before
 public void setup() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 testNode = mapper.readTree(json);
 }

 @Test
 public void testBasicFunctionality_StartObject() throws IOException {
 parser = new TreeTraversingParser(testNode);
 // Basic test for starting at the root of the JSON tree which is an object
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 @Test
 public void testBasicFunctionality_StartArray() throws IOException {
 parser = new TreeTraversingParser(Json.valueOf(new int[]{}));
 // Basic test for starting at an empty array
 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 }

 @Test
 public void testBasicFunctionality_ValueNode() throws IOException {
 parser = new TreeTraversingParser(Json.valueOf("Hello"));
 // Basic test for value node
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("Hello", parser.getText());
 }

 @Test
 public void testBasicFunctionality_EndObject() throws IOException {
 parser = new TreeTraversingParser(testNode);
 // Test for reaching the end of an object
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testBasicFunctionality_EndArray() throws IOException {
 parser = new TreeTraversingParser(Json.valueOf(new int[]{1, 2, 3}));
 // Test for reaching the end of an array
 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testSkipChildren_Objects() throws IOException {
 parser = new TreeTraversingParser(testNode);
 // Test for skipping the children of an object
 parser.nextToken(); // Start Object
 parser.nextToken(); // Field name: name
 parser.skipChildren();
 // Test that we are now at the next field: age
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 Assert.assertEquals("age", parser.getCurrentName());
 }

 @Test
 public void testSkipChildren_Arrays() throws IOException {
 parser = new TreeTraversingParser(Json.valueOf(new int[]{1, 2, 3}));
 // Test for skipping the children of an array
 parser.nextToken(); // Start Array
 parser.nextToken(); // Value int: 1
 parser.skipChildren();
 // Test that we are now at the next value in the array: 2
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 }

 @Test(expected = JsonParseException.class)
 public void testExceptionHandling_NullNode() throws IOException {
 parser = new TreeTraversingParser(null);
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testExceptionHandling_UnexpectedToken() throws IOException {
 parser = new TreeTraversingParser(Json.valueOf(new int[]{1, 2, 3, 4}));
 // Test for unexpected token: 4 when expecting an end array
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 }

 @Test(expected = IllegalStateException.class)
 public void testExceptionHandling_ClosedParser() throws IOException {
 parser = new TreeTraversingParser(testNode);
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.close();
 parser.nextToken();
 }
}