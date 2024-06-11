package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonNode jsonNode;
 private TreeTraversingParser parser;

 @Before
 public void setUp() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(jsonFactory);
 JsonNode input = mapper.readTree("{\"name\": \"John\", \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\", \"Chicago\"]}");
 jsonNode = input;
 parser = new TreeTraversingParser(jsonNode);
 }

 @Test
 public void testNextToken_NormalCase() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", parser.getText());
 }

 @Test
 public void testNextToken_StartContainer() throws Exception {
 parser.nextToken(); // consume "{"
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 }

 @Test
 public void testNextToken_EndContainer() throws Exception {
 parser.nextToken(); // consume "[", consume "New York"
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 }

 @Test
 public void testNextToken_Null() throws Exception {
 parser.nextToken(); // consume "Chicago"
 parser.nextToken(); // consume "]"
 parser.nextToken(); // consume "}"
 JsonToken token = parser.nextToken();
 assertNull(token);
 }

 @Test(expected = IOException.class)
 public void testNextToken_IOException() throws Exception {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken(); // consume "}"
 parser.nextToken(); // exception should be thrown here
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_JsonParseException() throws Exception {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 JsonNode invalidJson = jsonNode.deepClone();
 invalidJson.set("name", JsonNode.Value.NUMBER);
 parser = new TreeTraversingParser(invalidJson);
 JsonToken token = parser.nextToken();
 assertNull(token);
 }
}