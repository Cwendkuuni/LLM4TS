package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class JsonParserSequenceTest {

 private JsonParser sequence;
 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser parser3;

 @Before
 public void setUp() throws IOException {
 parser1 = new JsonTestParser();
 parser2 = new JsonTestParser();
 parser3 = new JsonTestParser();
 sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(3, sequence.containedParsersCount());
 }

 @Test
 public void testNextToken_FirstParserReturnsToken() throws IOException {
 JsonToken token = sequence.nextToken();
 assertSame(parser1, sequence.getDelegate());
 assertEquals(JsonToken.START_ARRAY, token.getClass().getSimpleName());
 }

 @Test
 public void testNextToken_FirstParserReturnsNull() throws IOException {
 JsonToken token = sequence.nextToken();
 assertSame(parser1, sequence.getDelegate());
 assertNull(token);
 }

 @Test
 public void testNextToken_SecondParserReturnsToken() throws IOException {
 JsonToken token = sequence.nextToken();
 assertSame(parser2, sequence.getDelegate());
 assertEquals(JsonToken.VALUE_STRING, token.getClass().getSimpleName());
 }

 @Test
 public void testNextToken_SecondParserReturnsNull() throws IOException {
 JsonToken token = sequence.nextToken();
 assertSame(parser2, sequence.getDelegate());
 assertNull(token);
 }

 @Test
 public void testNextToken_ThirdParserReturnsToken() throws IOException {
 JsonToken token = sequence.nextToken();
 assertSame(parser3, sequence.getDelegate());
 assertEquals(JsonToken.END_ARRAY, token.getClass().getSimpleName());
 }

 @Test
 public void testNextToken_ThirdParserReturnsNull() throws IOException {
 JsonToken token = sequence.nextToken();
 assertSame(parser3, sequence.getDelegate());
 assertNull(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_NoParsersLeft() throws IOException {
 sequence.nextToken();
 sequence.nextToken();
 sequence.nextToken();
 sequence.nextToken();
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 assertEquals(0, sequence.getDelegate().getCurrentLocation().getLineNumber());
 }

 @After
 public void tearDown() throws IOException {
 parser1.close();
 parser2.close();
 parser3.close();
 }
}