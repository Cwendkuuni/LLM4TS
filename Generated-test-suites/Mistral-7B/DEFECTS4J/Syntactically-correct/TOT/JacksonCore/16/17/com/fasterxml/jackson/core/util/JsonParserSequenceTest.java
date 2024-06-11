package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private static final String TEST_INPUT = "{\"a\":1,\"b\":2,\"c\":3}";

 @Test
 public void testNextTokenWithSingleParser() throws IOException, JsonParseException {
 JsonParser parser = new JsonFactory().createParser(TEST_INPUT);
 JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
 JsonToken token = sequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("a", sequence.getText());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(1, sequence.getIntValue());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("b", sequence.getText());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(2, sequence.getIntValue());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("c", sequence.getText());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(3, sequence.getIntValue());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test
 public void testNextTokenWithTwoParsers() throws IOException, JsonParseException {
 JsonParser parser1 = new JsonFactory().createParser(TEST_INPUT);
 JsonParser parser2 = new JsonFactory().createParser(TEST_INPUT);
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

 JsonToken token = sequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("a", sequence.getText());

 // Check if both parsers read the same token
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(1, sequence.getIntValue());
 Assert.assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());

 // Check if the next token is read from the second parser
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("b", sequence.getText());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(2, sequence.getIntValue());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("c", sequence.getText());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(3, sequence.getIntValue());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test
 public void testErrorHandlingWithTwoParsers() throws IOException, JsonParseException {
 JsonParser parser1 = new JsonFactory().createParser(TEST_INPUT);
 JsonParser parser2 = new JsonFactory().createParser(TEST_INPUT.substring(1)); // Incomplete JSON
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

 JsonToken token = sequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.FIELD_NAME, token);
 Assert.assertEquals("a", sequence.getText());

 token = sequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 Assert.assertEquals(1, sequence.getIntValue());

 // The second parser will throw an exception
 Assert.assertTrue(sequence.nextToken() instanceof JsonParseException);
 }
}