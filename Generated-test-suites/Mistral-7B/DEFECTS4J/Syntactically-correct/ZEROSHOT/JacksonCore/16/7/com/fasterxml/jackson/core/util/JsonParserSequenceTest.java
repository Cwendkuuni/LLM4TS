package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private JsonParserSequence sequence;
 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser parser3;

 @Before
 public void setUp() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 String json1 = "{\"a\":1}";
 String json2 = "{\"b\":2}";
 String json3 = "{\"c\":3}";
 parser1 = mapper.getFactory().createParser(json1);
 parser2 = mapper.getFactory().createParser(json2);
 parser3 = mapper.getFactory().createParser(json3);
 sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
 }

 @After
 public void tearDown() throws IOException {
 if (sequence != null) {
 sequence.close();
 }
 }

 @Test
 public void testContainsCorrectParsers() {
 JsonParser[] containedParsers = sequence.getContainedParsers();
 assertArrayEquals(new JsonParser[] { parser1, parser2, parser3 }, containedParsers);
 }

 @Test
 public void testCloseAllParsers() throws IOException {
 sequence.close();
 assertEquals(JsonToken.CLOSE_EVENT, sequence.nextToken());
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextTokenWithSingleParser() throws IOException {
 JsonToken token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextTokenWithTwoParsers() throws IOException {
 JsonToken token1 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token1);
 JsonToken token2 = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token2);
 assertEquals("a", token2.getText().toString());
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextTokenWithThreeParsers() throws IOException {
 JsonToken token1 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token1);
 JsonToken token2 = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token2);
 assertEquals("a", token2.getText().toString());
 JsonToken token3 = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token3);
 assertEquals(1, token3.getIntValue());
 assertNull(sequence.nextToken());
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonWithSingleParser() throws IOException {
 sequence.nextToken();
 sequence.nextToken(); // should fail
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonWithTwoParsers() throws IOException {
 sequence.nextToken();
 sequence.nextToken();
 sequence.nextToken(); // should fail
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonWithThreeParsers() throws IOException {
 sequence.nextToken();
 sequence.nextToken();
 sequence.nextToken();
 sequence.nextToken(); // should fail
 }

 @Test
 public void testSwitchParsers() throws IOException {
 JsonToken token1 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token1);
 sequence.switchToNext();
 JsonToken token2 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token2);
 sequence.switchToNext();
 JsonToken token3 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token3);
 assertNull(sequence.nextToken());
 }

 @Test
 public void testSwitchParsersBackToPreviousOne() throws IOException {
 JsonToken token1 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token1);
 sequence.switchToNext();
 JsonToken token2 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token2);
 sequence.switchToNext();
 JsonToken token3 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token3);
 sequence.switchToPrevious();
 JsonToken token4 = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token4);
 assertEquals(parser2, sequence.getDelegate());
 assertNull(sequence.nextToken());
 }
}