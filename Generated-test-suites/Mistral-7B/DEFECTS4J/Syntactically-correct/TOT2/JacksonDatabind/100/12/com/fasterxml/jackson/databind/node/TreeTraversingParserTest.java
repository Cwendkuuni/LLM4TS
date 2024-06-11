package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

 private ObjectMapper objectMapper;
 private TreeTraversingParser parser;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testEmptyJsonArray() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("[]");
 parser = new TreeTraversingParser(jsonNode);

 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(true, parser.isClosed());
 }

 @Test
 public void testEmptyJsonObject() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("{}");
 parser = new TreeTraversingParser(jsonNode);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals(true, parser.isClosed());
 }

 @Test
 public void testSimpleJsonArray() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("[1, 2, 3]");
 parser = new TreeTraversingParser(jsonNode);

 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(1, parser.getIntValue());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(2, parser.getIntValue());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(3, parser.getIntValue());

 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(true, parser.isClosed());
 }

 @Test
 public void testSimpleJsonObject() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("{\"name\": \"John\", \"age\": 30}");
 parser = new TreeTraversingParser(jsonNode);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("name", parser.getCurrentName());

 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("John", parser.getText());

 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("age", parser.getCurrentName());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());

 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals(true, parser.isClosed());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonArray() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("[1, 2]\"3");
 parser = new TreeTraversingParser(jsonNode);

 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(1, parser.getIntValue());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(2, parser.getIntValue());

 // Exception should be thrown here
 assertFalse(parser.isClosed());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonObject() throws JsonProcessingException {
 JsonNode jsonNode = objectMapper.readTree("{\"name\": \"John\", \"age\": 30\"}");
 parser = new TreeTraversingParser(jsonNode);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("name", parser.getCurrentName());

 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("John", parser.getText());

 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("age", parser.getCurrentName());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());

 // Exception should be thrown here
 assertFalse(parser.isClosed());
 }
}