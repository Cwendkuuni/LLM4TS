package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequenceParser;

 @Before
 public void setup() throws IOException {
 firstParser = new FileBasedStreamParser(new File("test1.json"));
 secondParser = new FileBasedStreamParser(new File("test2.json"));
 sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
 }

 @Test
 public void testSequentialParsing() throws IOException, JsonParseException {
 JsonToken token1 = sequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token1);
 JsonToken token2 = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token2);
 // Testing typical use case by checking if both parsers read expected tokens
 String expectedFieldName = "field1";
 String fieldName = null;
 if (token2.isStructStartElement()) {
 JsonParserFieldName pfn = new JsonParserFieldName(sequenceParser);
 fieldName = pfn.getText();
 }
 assertTrue("Field name not matched", fieldName != null && fieldName.equals(expectedFieldName));

 JsonToken token3 = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token3);
 String value = null;
 if (token3.isTextual()) {
 value = token3.getText();
 }
 assertTrue("Value not matched", value != null && value.equals("value1"));

 JsonToken eof = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, eof.asInt());
 }

 @Test
 public void testErrorHandling() throws IOException, JsonParseException {
 JsonToken token = sequenceParser.nextToken();
 assertNull("Unexpected token", token);
 sequenceParser.nextToken(); // Consume START_OBJECT
 sequenceParser.nextToken(); // Consume field name
 sequenceParser.nextToken(); // Consume VALUE_STRING

 // Test error handling by intentionally reading beyond EOF
 JsonToken unexpectedToken = sequenceParser.nextToken();
 assertTrue("Unexpected token", unexpectedToken == null);
 }

 @Test
 public void testSwitchingParsers() throws IOException, JsonParseException {
 JsonToken token = null;
 int containedParsersCount = sequenceParser.containedParsersCount();
 for (int i = 0; i < containedParsersCount; i++) {
 token = sequenceParser.nextToken();
 switch (token.asInt()) {
 case JsonToken.START_OBJECT:
 // Do some testing on first parser
 break;
 case JsonToken.FIELD_NAME:
 // Do some testing on second parser
 break;
 default:
 // Handle other cases
 }
 }
 // Testing that all parsers are consumed
 assertEquals(JsonToken.END_OBJECT, sequenceParser.nextToken().asInt());
 }

 @Test
 public void testNestedSequences() throws IOException, JsonParseException {
 JsonParser nestedSequenceParser = JsonParserSequence.createFlattened(firstParser, sequenceParser);
 // Testing if the sequence parser correctly reads tokens from nested sequences
 JsonToken token = nestedSequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.asInt());
 token = nestedSequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.asInt());
 String fieldName = null;
 if (token.isStructStartElement()) {
 JsonParserFieldName pfn = new JsonParserFieldName(nestedSequenceParser);
 fieldName = pfn.getText();
 }
 assertTrue("Nested field name not matched", fieldName != null && fieldName.equals("field1"));

 JsonToken valueToken = nestedSequenceParser.nextToken();
 String value = null;
 if (valueToken.isTextual()) {
 value = valueToken.getText();
 }
 assertTrue("Nested value not matched", value != null && value.equals("value1"));

 JsonToken eof = nestedSequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, eof.asInt());
 }
}