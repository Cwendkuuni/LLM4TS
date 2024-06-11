package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.base.ParserMinimalBase;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser sequence;

 @Before
 public void setUp() throws Exception {
 parser1 = mock(JsonParser.class);
 parser2 = mock(JsonParser.class);
 sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
 }

 @After
 public void tearDown() throws Exception {
 parser1 = null;
 parser2 = null;
 sequence = null;
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser parser1Sequence = mock(JsonParserSequence.class);
 when(parser1Sequence.containedParsersCount()).thenReturn(1);
 JsonParser parser2 = mock(JsonParser.class);
 JsonParser parser3 = mock(JsonParser.class);
 JsonParser expectedSequence = new JsonParserSequence(new JsonParser[] { parser1Sequence, parser2, parser3 });
 JsonParser actualSequence = JsonParserSequence.createFlattened(parser1Sequence, parser2);
 assertEquals(expectedSequence, actualSequence);
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 parser1.close();
 parser2.close();
 }

 @Test
 public void testNextToken() throws IOException {
 JsonToken token1 = JsonToken.START_OBJECT;
 JsonToken token2 = JsonToken.VALUE_STRING;
 when(parser1.nextToken()).thenReturn(token1);
 when(parser2.nextToken()).thenReturn(token2);
 JsonToken actualToken = sequence.nextToken();
 assertEquals(token1, actualToken);
 actualToken = sequence.nextToken();
 assertEquals(token2, actualToken);
 assertNull(sequence.nextToken());
 }

 @Test(expected = IOException.class)
 public void testNextTokenIOException() throws IOException {
 when(parser1.nextToken()).thenThrow(new IOException("Test IOException"));
 sequence.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenJsonParseException() throws IOException {
 when(parser1.nextToken()).thenThrow(new JsonParseException("Test JsonParseException"));
 sequence.nextToken();
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testSwitchToNext() throws IOException {
 ParserMinimalBase parser1Mock = mock(ParserMinimalBase.class);
 ParserMinimalBase parser2Mock = mock(ParserMinimalBase.class);
 JsonParser delegate = parser1Mock;
 JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1Mock, parser2Mock });
 assertEquals(parser1Mock, sequence.delegate);
 assertFalse(sequence.switchToNext());
 delegate = parser2Mock;
 assertTrue(sequence.switchToNext());
 assertEquals(parser2Mock, sequence.delegate);
 }
}