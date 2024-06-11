package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser sequence;

 @Before
 public void setup() throws IOException {
 JsonFactory factory = new JsonFactory();
 parser1 = factory.createParser("[]");
 parser2 = factory.createParser("[1, 2, 3]");
 sequence = JsonParserSequence.createFlattened(parser1, parser2);
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testNextToken_whenBothParsersReturnNull_shouldReturnNull() throws IOException {
 assertNull(sequence.nextToken());
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextToken_whenFirstParserReturnsToken_shouldReturnThatToken() throws IOException {
 JsonToken token = sequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NULL, token);
 }

 @Test
 public void testNextToken_whenFirstParserReturnsTokenAndSecondParserReturnsNull_shouldReturnThatToken() throws IOException {
 JsonToken token = sequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 assertNull(sequence.nextToken());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NULL, token);
 }

 @Test
 public void testNextToken_whenFirstParserReturnsTokenAndSecondParserReturnsToken_shouldReturnThatToken() throws IOException {
 JsonToken token1 = sequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token1);
 JsonToken token2 = sequence.nextToken();
 assertEquals(JsonToken.VALUE_INTEGER, token2);
 assertEquals(1, token2.asInt());
 token2 = sequence.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token2);
 assertEquals("1", token2.asText());
 token2 = sequence.nextToken();
 assertEquals(JsonToken.VALUE_INTEGER, token2);
 assertEquals(2, token2.asInt());
 token2 = sequence.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token2);
 assertEquals("2", token2.asText());
 token2 = sequence.nextToken();
 assertEquals(JsonToken.VALUE_INTEGER, token2);
 assertEquals(3, token2.asInt());
 token2 = sequence.nextToken();
 assertNull(token2);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_whenBothParsersReturnError_shouldThrowException() throws IOException {
 sequence.nextToken();
 sequence.nextToken(); // Should throw an exception
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 parser1.close();
 parser2.close();
 }

 @Test
 public void testErrorHandling() throws IOException {
 sequence.nextToken();
 parser1.setText("invalid JSON");
 sequence.nextToken(); // Should throw an exception
 }

 @Test
 public void testErrorHandling2() throws IOException {
 sequence.nextToken();
 sequence.nextToken();
 parser2.setText("invalid JSON");
 sequence.nextToken(); // Should use parser1
 }
}