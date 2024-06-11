package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.databind.*;

public class JsonParserSequenceTest {

 private JsonParser sequence;
 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser parser3;

 @Before
 public void setUp() throws IOException {
 JsonParserFactory factory = new DefaultParserFactory();
 parser1 = factory.getParser("[]");
 parser2 = factory.getParser("{\"a\":1,\"b\":[2,3]}");
 parser3 = factory.getParser("{\"c\":[4,5]}");
 sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
 }

 @After
 public void tearDown() throws IOException {
 sequence.close();
 parser1.close();
 parser2.close();
 parser3.close();
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(3, sequence.containedParsersCount());
 }

 @Test
 public void testClose() throws IOException {
 parser1.nextToken();
 parser2.nextToken();
 parser3.nextToken();
 sequence.close();
 assertEquals(JsonToken.INVALID_TOKEN, sequence.nextToken());
 assertEquals(JsonToken.INVALID_TOKEN, sequence.nextToken());
 assertEquals(JsonToken.INVALID_TOKEN, sequence.nextToken());
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 JsonToken token;

 // Test when all parsers are empty
 token = sequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 token = sequence.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("a", token.getValueAsString());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(1, token.getIntValue());
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("b", token.getValueAsString());
 token = sequence.nextToken();
 assertTrue(token.isStartArray());

 // Test when one parser has a token
 parser1.nextToken();
 parser1.nextToken(); // Consume [ and ]
 token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 // Test when another parser has a token
 parser2.nextToken();
 parser2.nextToken(); // Consume {" and "a"
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("1", token.getValueAsString());

 // Test when the third parser has a token
 parser3.nextToken();
 parser3.nextToken(); // Consume {" and "c"
 token = sequence.nextToken();
 assertTrue(token.isStartArray());
}
}