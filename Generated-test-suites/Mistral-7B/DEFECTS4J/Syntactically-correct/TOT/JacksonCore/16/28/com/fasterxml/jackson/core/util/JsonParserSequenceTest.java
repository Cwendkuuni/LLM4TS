package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.io.SerializationTest;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonParserSequenceTest {

 private JsonParser[] parsers;
 private JsonParserSequence seq;

 @Before
 public void setup() {
 JsonParser parser1 = new ParserMinimalBase(new ByteArrayInputStream("{\"a\":1}".getBytes()));
 JsonParser parser2 = new ParserMinimalBase(new ByteArrayInputStream("{\"b\":2}".getBytes()));
 parsers = new JsonParser[] { parser1, parser2 };
 seq = new JsonParserSequence(parsers);
 }

 @Test
 public void testNextTokenNormalCase() throws IOException {
 JsonToken expectedToken;
 JsonToken actualToken;
 expectedToken = JsonToken.START_OBJECT;
 actualToken = seq.nextToken();
 assertEquals(expectedToken, actualToken);

 expectedToken = JsonToken.FIELD_NAME;
 actualToken = seq.nextToken();
 assertEquals("a", actualToken.getText());

 expectedToken = JsonToken.VALUE_NUMBER;
 actualToken = seq.nextToken();
 assertEquals(1, actualToken.asInt());

 expectedToken = JsonToken.END_OBJECT;
 actualToken = seq.nextToken();
 assertEquals(expectedToken, actualToken);
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenErrorScenario() throws IOException {
 seq.nextToken(); // consume first token
 seq.nextToken(); // consume second token

 // this should throw an exception since there are no more parsers
 seq.nextToken();
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(2, seq.containedParsersCount());
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser parser3 = new ParserMinimalBase(new ByteArrayInputStream("{\"c\":3}".getBytes()));
 JsonParser parser4 = new ParserMinimalBase(new ByteArrayInputStream("{\"d\":4}".getBytes()));
 JsonParser flattenedParser = JsonParserSequence.createFlattened(parsers[0], parser2, parser3, parser4);
 assertEquals(4, flattenedParser.containedParsersCount());
 }

 @Test
 public void testAddFlattenedActiveParsers() {
 List<JsonParser> parsersList = new ArrayList<JsonParser>();
 JsonParserSequence sequence = new JsonParserSequence(new JsonParser[0]);
 sequence.addFlattenedActiveParsers(parsersList);
 JsonParser parser1 = new ParserMinimalBase(new ByteArrayInputStream("{\"a\":1}".getBytes()));
 JsonParser parser2 = new ParserMinimalBase(new ByteArrayInputStream("{\"b\":2}".getBytes()));
 sequence.addFlattenedActiveParsers(parsersList);
 sequence.addFlattenedActiveParsers(parsers);
 List<JsonParser> expectedParsers = Arrays.asList(parsers[0], parser1, parser2);
 assertEquals(expectedParsers, parsersList);
 }

 @Test
 public void testClose() throws IOException {
 seq.close();
 for (JsonParser parser : parsers) {
 parser.close();
 }
 }
}