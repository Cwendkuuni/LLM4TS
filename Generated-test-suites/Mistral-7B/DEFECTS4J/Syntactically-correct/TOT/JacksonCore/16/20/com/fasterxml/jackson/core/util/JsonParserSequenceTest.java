package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParserSequence parserSequence;

 @Before
 public void setup() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 parser1 = jsonFactory.createParser("{ \"key1\": 1 }");
 parser2 = jsonFactory.createParser("{ \"key2\": 2 }");
 parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
 }

 @Test
 public void testNextToken_FirstParser_ReturnsExpectedToken() throws Exception {
 JsonToken token = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);
 }

 @Test
 public void testNextToken_FirstParser_ReadsWholeObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json1 = "{\"key1\":1,\"key2\":2}";
 mapper.readTree(parserSequence);
 JsonToken token = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test
 public void testNextToken_SecondParser_ReturnsExpectedToken() throws Exception {
 JsonToken token = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);
 parserSequence.nextToken(); // consume first key-value pair
 token = parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token); // should be the second object
 }

 @Test(expected = IOException.class)
 public void testNextToken_EndOfStream_ThrowsIOException() throws Exception {
 parserSequence.close(); // close both parsers
 JsonToken token = parserSequence.nextToken(); // should throw IOException
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_InvalidJson_ThrowsJsonParseException() throws Exception {
 parser1 = jsonFactory.createParser("{ \"key1\": 1"); // incomplete json
 parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonToken token = parserSequence.nextToken(); // should throw JsonParseException
 }
}