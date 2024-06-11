package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequenceParser;

 @Before
 public void setup() throws Exception {
 firstParser = new ObjectMapper().getFactory().createParser("[]");
 secondParser = new ObjectMapper().getFactory().createParser("{}");
 sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
 }

 @Test
 public void testNextToken() throws Exception {
 JsonToken token;

 // Test with first parser
 token = sequenceParser.nextToken();
 Assert.assertEquals(JsonToken.START_ARRAY, token);

 // Test with second parser
 token = sequenceParser.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);

 // Test with both parsers
 token = sequenceParser.nextToken();
 Assert.assertEquals(JsonToken.VALUE_STRING, token);

 // Test end of first parser
 token = sequenceParser.nextToken();
 Assert.assertNull(token);

 // Test end of second parser
 token = sequenceParser.nextToken();
 Assert.assertNull(token);

 // Test error handling
 JsonParseException exception = Assert.assertThrows(JsonParseException.class, () -> sequenceParser.nextToken());
 String expectedErrorMessage = "_value is not recognized (expected: START_OBJECT)";
 Assert.assertEquals(expectedErrorMessage, exception.getMessage());
 }

 @Test
 public void testClose() throws Exception {
 sequenceParser.close();
 }

 @Test(expected = IOException.class)
 public void testCloseTwice() throws Exception {
 sequenceParser.close();
 sequenceParser.close();
 }

 @Test
 public void testContainedParsersCount() {
 Assert.assertEquals(2, sequenceParser.containedParsersCount());
 }
}