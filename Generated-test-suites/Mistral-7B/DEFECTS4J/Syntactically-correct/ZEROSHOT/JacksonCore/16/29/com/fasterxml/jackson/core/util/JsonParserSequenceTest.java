package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequenceParser;

 @Before
 public void setup() throws IOException {
 firstParser = new JsonParserFactory().createParser(new StringReader("{\"name\": \"John\", \"age\": 30}"));;
 secondParser = new JsonParserFactory().createParser(new StringReader("{\"name\": \"Jane\", \"age\": 25}"));;
 sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(2, sequenceParser.containedParsersCount());
 }

 @Test
 public void testNextTokenWithOneParser() throws IOException {
 JsonToken token = sequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getClass());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getClass());
 assertEquals("name", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token.getClass());
 assertEquals("John", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getClass());
 assertEquals("age", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getClass());
 assertEquals(30, token.getIntValue());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getClass());
 }

 @Test
 public void testNextTokenWithTwoParsers() throws IOException {
 JsonToken token = sequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getClass());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getClass());
 assertEquals("name", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token.getClass());
 assertEquals("John", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getClass());
 assertEquals("age", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getClass());
 assertEquals(30, token.getIntValue());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getClass());
 assertEquals("name", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token.getClass());
 assertEquals("Jane", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getClass());
 assertEquals("age", token.getText().toString());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getClass());
 assertEquals(25, token.getIntValue());
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getClass());
 }

 @Test(expected = IOException.class)
 public void testNextTokenWithIOException() throws IOException, JsonParseException {
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 firstParser.close();
 secondParser.nextToken(); // this should throw an IOException
 sequenceParser.nextToken();
 }

 @Test
 public void testClose() throws IOException {
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.close();
 firstParser.close();
 secondParser.close();
 }
}