package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper mapper;
 private JsonFactory jsonFactory;
 private TreeTraversingParser parser;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 jsonFactory = mapper.getFactory();
 }

 // Test constructor with null and valid JsonNode
 @Test
 public void testTreeTraversingParserConstructorNull() {
 assertNull(new TreeTraversingParser(null));
 JsonNode validJsonNode = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}").getParser();
 TreeTraversingParser validParser = new TreeTraversingParser(validJsonNode);
 }

 // Test getters and setters
 @Test
 public void testGettersAndSetters() {
 JsonNode jsonNode = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}").getParser();
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 assertEquals(jsonNode, parser.getNode());
 assertEquals(mapper, parser.getCodec());
 parser.setCodec(new ObjectMapper());
 assertNotEquals(mapper, parser.getCodec());
 }

 // Test nextToken() method with valid JsonNode
 @Test
 public void testNextTokenValidJsonNode() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}").getParser();
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken expectedToken = JsonToken.FIELD_NAME;
 JsonToken actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_STRING;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.FIELD_NAME;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_NUMBER_INT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 }

 // Test nextToken() method with empty JsonNode
 @Test
 public void testNextTokenEmptyJsonNode() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("{}").getParser();
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken expectedToken = JsonToken.START_OBJECT;
 JsonToken actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.END_OBJECT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 }

 // Test nextToken() method with JsonNode containing array
 @Test
 public void testNextTokenJsonNodeContainingArray() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("[1, 2, 3]").getParser();
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken expectedToken = JsonToken.START_ARRAY;
 JsonToken actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_NUMBER_INT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_NUMBER_INT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_NUMBER_INT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.END_ARRAY;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 }

 // Test nextToken() method with JsonNode containing object
 @Test
 public void testNextTokenJsonNodeContainingObject() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}").getParser();
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 JsonToken expectedToken = JsonToken.START_OBJECT;
 JsonToken actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.FIELD_NAME;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_STRING;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.FIELD_NAME;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.VALUE_NUMBER_INT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 expectedToken = JsonToken.END_OBJECT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 }

 // Test nextToken() method with JsonNode containing invalid Json
 @Test(expected = JsonParseException.class)
 public void testNextTokenJsonNodeContainingInvalidJson() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("{\"name:\"John\",\"age\":30}").getParser(); // missing colon in name
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 parser.nextToken();
 }

 // Test skipChildren() method
 @Test
 public void testSkipChildren() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}").getParser();
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 parser.nextToken(); // FIELD_NAME: "name"
 parser.nextToken(); // VALUE_STRING: "John"
 parser.nextToken(); // FIELD_NAME: "age"
 parser.nextToken(); // VALUE_NUMBER_INT: 30
 parser.skipChildren(); // Skip "hobbies" array
 JsonToken expectedToken = JsonToken.FIELD_NAME;
 JsonToken actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken); // "age" should be skipped
 expectedToken = JsonToken.FIELD_NAME;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken); // "hobbies" should be skipped
 expectedToken = JsonToken.END_OBJECT;
 actualToken = parser.nextToken();
 assertEquals(expectedToken, actualToken);
 }

 // Test isClosed() method
 @Test
 public void testIsClosed() throws IOException {
 JsonNode jsonNode = jsonFactory.createParser("{\"name\":\"John\",\"age\":30}").getParser();
 TreeTraversingParser parser1 = new TreeTraversingParser(jsonNode);
 TreeTraversingParser parser2 = new TreeTraversingParser(jsonNode);
 assertFalse(parser1.isClosed());
 assertFalse(parser2.isClosed());
 parser1.close();
 assertTrue(parser1.isClosed());
 assertTrue(parser2.isClosed());
 }
}