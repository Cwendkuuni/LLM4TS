package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParserSequence sequence;

 @Before
 public void setup() throws IOException {
 parser1 = new JsonFactory().createParser("[]");
 parser2 = new JsonFactory().createParser("{\"foo\":1}");
 sequence = JsonParserSequence.createFlattened(parser1, parser2);
 }

 @Test
 public void testContainedParsersCount() throws IOException {
 Assert.assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testNextToken_FirstParserHasToken() throws IOException {
 JsonToken token = sequence.nextToken();
 Assert.assertNotNull(token);
 Assert.assertEquals(JsonToken.START_ARRAY, token.getClass());
 }

 @Test
 public void testNextToken_SecondParserHasToken() throws IOException {
 JsonToken token = sequence.nextToken();
 Assert.assertNotNull(token);
 Assert.assertEquals(JsonToken.FIELD_NAME, token.getClass());
 Assert.assertEquals("foo", token.getText());
 }

 @Test
 public void testNextToken_BothParsersEmpty() throws IOException {
 JsonToken token = sequence.nextToken();
 Assert.assertNull(token);
 }

 @Test(expected = IOException.class)
 public void testNextToken_FirstParserThrowsException() throws IOException {
 sequence.delegate.nextToken(); // consume first token
 sequence.nextToken(); // should throw exception
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_SecondParserThrowsException() throws IOException {
 sequence.delegate = parser1; // switch to first parser
 sequence.nextToken(); // consume first token
 sequence.nextToken(); // should throw exception
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 Assert.assertFalse(parser1.isOpen());
 Assert.assertFalse(parser2.isOpen());
 }

 @Test
 public void testSwitchToNextParser_AllParsersHaveTokens() throws IOException {
 JsonToken token;
 for (int i = 0; i < 10; i++) {
 token = sequence.nextToken();
 Assert.assertNotNull(token);
 }
 }
}