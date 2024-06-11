package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequence;

 @Before
 public void setup() {
 firstParser = mock(JsonParser.class);
 secondParser = mock(JsonParser.class);
 sequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser firstSequence = mock(JsonParserSequence.class);
 when(firstSequence.containedParsersCount()).thenReturn(2);
 JsonParser secondSequence = mock(JsonParserSequence.class);
 when(secondSequence.containedParsersCount()).thenReturn(3);

 JsonParser expectedSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, secondSequence.delegate, firstSequence.delegate });
 assertEquals(expectedSequence, JsonParserSequence.createFlattened(firstSequence, secondSequence));
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 }

 @Test
 public void testNextTokenFromFirstParser() throws IOException {
 JsonToken token = JsonToken.VALUE_STRING;
 when(firstParser.nextToken()).thenReturn(token);
 when(secondParser.nextToken()).thenReturn(null);
 JsonToken actualToken = sequence.nextToken();
 assertEquals(token, actualToken);
 }

 @Test
 public void testNextTokenFromSecondParser() throws IOException {
 JsonToken token = JsonToken.START_OBJECT;
 when(firstParser.nextToken()).thenReturn(null);
 when(secondParser.nextToken()).thenReturn(token);
 JsonToken actualToken = sequence.nextToken();
 assertEquals(token, actualToken);
 }

 @Test(expected = JsonParseException.class)
 public void testNullTokenFromBothParsers() throws IOException {
 when(firstParser.nextToken()).thenReturn(null);
 when(secondParser.nextToken()).thenReturn(null);
 sequence.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testIOExceptionFromBothParsers() throws IOException {
 when(firstParser.nextToken()).thenThrow(new IOException("Error from first parser"));
 when(secondParser.nextToken()).thenThrow(new IOException("Error from second parser"));
 sequence.nextToken();
 }

 @Test
 public void testContainedParsersCount() throws IOException {
 assertEquals(2, sequence.containedParsersCount());
 }
}