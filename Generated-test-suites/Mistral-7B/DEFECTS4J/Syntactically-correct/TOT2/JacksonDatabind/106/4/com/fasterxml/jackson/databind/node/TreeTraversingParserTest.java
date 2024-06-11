package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private ObjectCodec objectCodec;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 objectCodec = new ObjectMapper().getFactory().createObjectCodec();
 parser = new TreeTraversingParser(new ObjectNode(objectCodec), objectCodec);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws IOException {
 parser.close();
 }

 @Test
 public void testEmptyParser() throws IOException {
 JsonNode rootNode = new ObjectNode(objectCodec);
 parser = new TreeTraversingParser(rootNode, objectCodec);
 JsonToken token = parser.nextToken();
 assertNull(token);
 parser.close();
 }

 @Test
 public void testParseStartArray() throws IOException {
 JsonNode arrayNode = new ArrayNode(objectCodec);
 parser = new TreeTraversingParser(arrayNode, objectCodec);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 token = parser.nextToken();
 assertTrue(token == null || token.isStructStart());
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 token = parser.nextToken();
 assertFalse(token.isStructEnd());
 parser.close();
 }

 @Test
 public void testParseStartObject() throws IOException {
 JsonNode objectNode = new ObjectNode(objectCodec);
 objectNode.put("name", new TextNode("John"));
 objectNode.put("age", new NumberNode(30));
 parser = new TreeTraversingParser(objectNode, objectCodec);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 token = parser.nextToken();
 assertEquals("name", token.asString());
 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", token.getText());
 token = parser.nextToken();
 assertEquals("age", token.asString());
 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(30, token.getIntValue());
 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 parser.close();
 }

 @Test(expected = JsonParseException.class)
 public void testParseInvalidJson() throws IOException {
 parser = new TreeTraversingParser(new TextNode("{foo:bar"), objectCodec);
 parser.nextToken();
 }

 @Test
 public void testSkipChildren() throws IOException {
 JsonNode arrayNode = new ArrayNode(objectCodec);
 arrayNode.add(new ObjectNode(objectCodec).put("name", new TextNode("Alice")))
         .add(new ObjectNode(objectCodec).put("name", new TextNode("Bob")));
 parser = new TreeTraversingParser(arrayNode, objectCodec);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 parser.skipChildren();
 token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 parser.close();
 }

 @Test
 public void testReadBinaryValue() throws IOException {
 JsonNode binaryNode = new TextNode("iVBORw0KGgoAAAANSUhEUgAAAT4AAACeCAMAAAD1Svh...");
 byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD, outputStream);
 assertEquals(binaryData.length, 3072);
 byte[] expectedBinaryData = new byte[3072]; // replace with actual binary data
 assertArrayEquals(expectedBinaryData, binaryData);
 }

 @Test
 public void testReadBinaryValueWithEmptyData() throws IOException {
 parser = new TreeTraversingParser(new ObjectNode(objectCodec), objectCodec);
 parser.nextToken(); // SKIP_VALUE
 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME
 parser.nextToken(); // VALUE_NULL
 parser.nextToken(); // END_OBJECT
 byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD, outputStream);
 assertEquals(0, binaryData.length);
 }

 @Test
 public void testGetText() throws IOException {
 JsonNode stringNode = new TextNode("Hello World");
 parser = new TreeTraversingParser(stringNode, objectCodec);
 assertEquals("Hello World", parser.getText());
 parser.close();
 }

 @Test
 public void testGetTextWithEmptyData() throws IOException {
 parser = new TreeTraversingParser(JsonToken.NULL, objectCodec);
 assertNull(parser.getText());
 parser.close();
 }

 @Test
 public void testGetTextWithInvalidData() throws IOException {
 parser = new TreeTraversingParser(new NumberNode(42), objectCodec);
 assertNull(parser.getText());
 parser.close();
 }

 @Test
 public void testGetTextCharacters() throws IOException {
 JsonNode stringNode = new TextNode("Hello World");
 parser = new TreeTraversingParser(stringNode, objectCodec);
 char[] textCharacters = parser.getTextCharacters();
 assertEquals("Hello World".length(), textCharacters.length);
 assertEquals('H', textCharacters[0]);
 assertEquals('l', textCharacters[5]);
 parser.close();
 }

 @Test
 public void testGetTextCharactersWithEmptyData() throws IOException {
 parser = new TreeTraversingParser(JsonToken.NULL, objectCodec);
 assertNull(parser.getTextCharacters());
 parser.close();
 }

 @Test
 public void testGetTextLength() throws IOException {
 JsonNode stringNode = new TextNode("Hello World");
 parser = new TreeTraversingParser(stringNode, objectCodec);
 assertEquals("Hello World".length(), parser.getTextLength());
 parser.close();
 }

 @Test
 public void testGetTextLengthWithEmptyData() throws IOException {
 parser = new TreeTraversingParser(JsonToken.NULL, objectCodec);
 assertEquals(0, parser.getTextLength());
 parser.close();
 }

 @Test(expected = JsonParseException.class)
 public void testGetTextWithInvalidData() throws IOException {
 parser = new TreeTraversingParser(new NumberNode(42), objectCodec);
 parser.getText();
 }

 @Test
 public void testGetNumberType() throws IOException {
 JsonNode numberNode = new NumberNode(42.5);
 parser = new TreeTraversingParser(numberNode, objectCodec);
 assertEquals(JsonType.NUMBER, parser.getNumberType());
 parser.close();
 }

 @Test
 public void testGetNumberTypeWithInvalidData() throws IOException {
 parser = new TreeTraversingParser(new TextNode(""), objectCodec);
 assertEquals(null, parser.getNumberType());
 parser.close();
 }

 @Test
 public void testGetBigIntegerValue() throws IOException {
 JsonNode bigIntegerNode = new NumberNode(new BigInteger("123456789012345678901234567890"));
 parser = new TreeTraversingParser(bigIntegerNode, objectCodec);
 BigInteger value = parser.getBigIntegerValue();
 assertEquals(new BigInteger("123456789012345678901234567890"), value);
 parser.close();
 }

 @Test
 public void testGetDecimalValue() throws IOException {
 JsonNode decimalNode = new NumberNode(new BigDecimal("123.45"));
 parser = new TreeTraversingParser(decimalNode, objectCodec);
 BigDecimal value = parser.getDecimalValue();
 assertEquals(new BigDecimal("123.45"), value);
 parser.close();
 }

 @Test
 public void testGetDoubleValue() throws IOException {
 JsonNode doubleNode = new NumberNode(42.5);
 parser = new TreeTraversingParser(doubleNode, objectCodec);
 assertEquals(42.5, parser.getDoubleValue(), 0.0);
 parser.close();
 }

 @Test
 public void testGetFloatValue() throws IOException {
 JsonNode floatNode = new NumberNode(42.5f);
 parser = new TreeTraversingParser(floatNode, objectCodec);
 assertEquals(42.5f, parser.getFloatValue(), 0.0f);
 parser.close();
 }

 @Test
 public void testGetIntValue() throws IOException {
 JsonNode intNode = new NumberNode(42);
 parser = new TreeTraversingParser(intNode, objectCodec);
 assertEquals(42, parser.getIntValue());
 parser.close();
 }

 @Test
 public void testGetLongValue() throws IOException {
 JsonNode longNode = new NumberNode(42L);
 parser = new TreeTraversingParser(longNode, objectCodec);
 assertEquals(42L, parser.getLongValue());
 parser.close();
 }

 @Test
 public void testGetNumberValue() throws IOException {
 JsonNode numberNode = new NumberNode(new BigDecimal("123.45"));
 parser = new TreeTraversingParser(numberNode, objectCodec);
 Number value = parser.getNumberValue();
 assertEquals(new BigDecimal("123.45"), value);
 parser.close();
 }

 @Test
 public void testGetEmbeddedObject() throws IOException {
 JsonNode objectNode = new ObjectNode(objectCodec)
         .put("name", new TextNode("Alice"))
         .put("age", new NumberNode(30))
         .put("city", new TextNode("New York"));
 parser = new TreeTraversingParser(objectNode, objectCodec);
 Object value = parser.getEmbeddedObject();
 assertEquals("Alice", value.getClass().getField("name").get(value));
 assertEquals(30, value.getClass().getField("age").get(value));
 assertEquals("New York", value.getClass().getField("city").get(value));
 parser.close();
 }

 @Test
 public void testIsNaN() throws IOException {
 JsonNode nanNode = new TextNode("NaN");
 parser = new TreeTraversingParser(nanNode, objectCodec);
 assertTrue(parser.isNaN());
 parser.close();
 }

 @Test
 public void testIsNaNWithValidData() throws IOException {
 JsonNode numberNode = new NumberNode(Double.NaN);
 parser = new TreeTraversingParser(numberNode, objectCodec);
 assertTrue(parser.isNaN());
 parser.close();
 }

 @Test
 public void testIsClosed() throws IOException {
 JsonNode arrayNode = new ArrayNode(objectCodec);
 parser = new TreeTraversingParser(arrayNode, objectCodec);
 assertFalse(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 }
}