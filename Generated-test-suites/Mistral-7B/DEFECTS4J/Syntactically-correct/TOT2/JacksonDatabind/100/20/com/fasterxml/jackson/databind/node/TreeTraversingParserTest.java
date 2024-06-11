package com.example.junit4;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

 private static final JsonFactory JSON_FACTORY = new JsonFactory();
 private static ObjectMapper objectMapper;

 static {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testEmptyJsonObject() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{}"));
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 parser.close();
 }

 @Test
 public void testEmptyJsonArray() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("[]"));
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 parser.skipChildren();
 parser.close();
 }

 @Test
 public void testJsonString() throws IOException {
 JsonNode jsonString = objectMapper.readTree("{\"str\": \"Hello World\"}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonString);
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("str", parser.getCurrentName());
 assertEquals("Hello World", parser.getText());
 parser.close();
 }

 @Test
 public void testJsonNumberInt() throws IOException {
 JsonNode jsonNumberInt = objectMapper.readTree("{ \"i\": 1 }");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNumberInt);
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("i", parser.getCurrentName());
 assertEquals(1, parser.getIntValue());
 parser.close();
 }

 @Test
 public void testJsonNumberFloat() throws IOException {
 JsonNode jsonNumberFloat = objectMapper.readTree("{ \"f\": 3.14 }");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNumberFloat);
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("f", parser.getCurrentName());
 assertEquals(3.14, parser.getDoubleValue(), 0.001);
 parser.close();
 }

 @Test
 public void testJsonArray() throws IOException {
 JsonNode jsonArray = objectMapper.readTree("[1, 2, 3]");
 TreeTraversingParser parser = new TreeTraversingParser(jsonArray);
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 parser.skipChildren(); // skip first element
 assertEquals(JsonToken.START_OBJECT, parser.nextToken()); // skip second element
 parser.skipChildren(); // skip third element
 parser.close();
 }

 @Test
 public void testJsonObject() throws IOException {
 JsonNode jsonObject = objectMapper.readTree("{\"a\": 1, \"b\": \"Hello\", \"c\": [1, 2], \"d\": {}}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonObject);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("a", parser.getCurrentName());
 assertEquals(1, parser.getIntValue());
 parser.nextToken(); // skip field "b"
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 parser.skipChildren(); // skip elements in array "c"
 assertEquals(JsonToken.START_OBJECT, parser.nextToken()); // skip object "d"
 parser.close();
 }

 @Test(expected = IOException.class)
 public void testNullJson() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 parser.nextToken(); // should throw IOException
 }

 @Test(expected = JsonParseException.class)
 public void testInvalidJson() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{"));
 parser.nextToken(); // should throw JsonParseException
 }
}