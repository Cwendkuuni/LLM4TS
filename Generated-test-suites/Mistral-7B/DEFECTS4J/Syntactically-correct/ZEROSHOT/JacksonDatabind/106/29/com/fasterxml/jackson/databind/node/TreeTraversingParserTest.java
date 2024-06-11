package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonCodec;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonNode node;
 private ObjectMapper mapper;
 private ObjectWriter writer;
 private JsonParser parser;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() throws IOException {
 mapper = new ObjectMapper();
 writer = mapper.writer(true);
 outputStream = new ByteArrayOutputStream();
 node = mapper.readTree("{\"name\":\"John\",\"age\":30,\"skills\":[\"Java\",\"JavaScript\"]}");
 }

 @Test
 public void testEmptyTree() throws IOException {
 parser = new TreeTraversingParser(null);
 assertEquals(JsonToken.START_NULL, parser.nextToken());
 parser.close();
 }

 @Test
 public void testValueNode() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree("Hello"));
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("Hello", parser.getText());
 parser.close();
 }

 @Test
 public void testArrayNode() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(new int[]{}));
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 parser.skipChildren();
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 parser.close();
 }

 @Test
 public void testObjectNode() throws IOException {
 parser = new TreeTraversingParser(node);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("John", parser.getText());
 assertEquals("age", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());
 parser.skipChildren();
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("skills", parser.getCurrentName());
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals("Java", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("JavaScript", parser.getText());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 parser.close();
 }

 @Test
 public void testNumericNode() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123));
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(123, parser.getIntValue());
 parser.close();
 }

 @Test
 public void testBinaryNode() throws IOException {
 byte[] binaryData = new byte[]{'T','e','st',' ', 'B','i','n','a','r','y',' ','N','o','d','e'};
 parser = new TreeTraversingParser(mapper.valueToTree(binaryData));
 assertEquals(JsonToken.VALUE_BINARY, parser.nextToken());
 assertEquals(binaryData.length, parser.getBinaryValue(Base64Variant.BASE64_STANDARD).length);
 parser.close();
 }

 @Test
 public void testSkipChildren() throws IOException {
 parser = new TreeTraversingParser(node);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 parser.skipChildren();
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 parser.close();
 }

 @Test(expected = JsonParseException.class)
 public void testClosedParser() throws IOException {
 parser = new TreeTraversingParser(node);
 parser.close();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNonExistingChild() throws IOException {
 parser = new TreeTraversingParser(node);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 parser.nextToken(); // skipped "name"
 parser.nextToken(); // skipped "age"
 parser.nextToken(); // skipped "skills" (ARRAY)
 parser.nextToken(); // skipped first element "Java"
 parser.nextToken(); // skipped second element "JavaScript" (END ARRAY)
 parser.nextToken(); // skipped END_OBJECT
 parser.nextToken(); // should throw exception
 parser.close();
 }

 @Test
 public void testReadBinaryValue() throws IOException {
 byte[] binaryData = new byte[]{'T','e','st',' ', 'B','i','n','a','r','y',' ','N','o','d','e'};
 parser = new TreeTraversingParser(mapper.valueToTree(binaryData));
 byte[] readData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
 assertEquals(binaryData.length, readData.length);
 parser.close();
 }

 @Test
 public void testReadBinaryValueWithWrite() throws IOException {
 byte[] binaryData = new byte[]{'T','e','st',' ', 'B','i','n','a','r','y',' ','N','o','d','e'};
 parser = new TreeTraversingParser(mapper.valueToTree(binaryData));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 parser.readBinaryValue(Base64Variant.BASE64_STANDARD, outputStream);
 assertEquals(binaryData.length, outputStream.size());
 parser.close();
 }

 @Test
 public void testObjectCodec() throws IOException {
 JsonCodec codec = mock(JsonCodec.class);
 parser = new TreeTraversingParser(node, codec);
 assertEquals(codec, parser.getCodec());
 parser.close();
 }

 @Test
 public void testIsClosed() throws IOException {
 parser = new TreeTraversingParser(node);
 assertFalse(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 parser.nextToken(); // should throw exception
 }

 @Test
 public void testGetParsingContext() throws IOException {
 parser = new TreeTraversingParser(node);
 assertSame(node, parser.getParsingContext());
 parser.close();
 }

 @Test
 public void testGetTokenLocation() throws IOException {
 parser = new TreeTraversingParser(node);
 assertSame(JsonLocation.NA, parser.getTokenLocation());
 parser.close();
 }

 @Test
 public void testGetCurrentLocation() throws IOException {
 parser = new TreeTraversingParser(node);
 assertSame(JsonLocation.NA, parser.getCurrentLocation());
 parser.close();
 }

 @Test
 public void testGetText() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree("Hello"));
 assertEquals("Hello", parser.getText());
 parser.close();
 }

 @Test
 public void testGetTextCharacters() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree("Hello"));
 assertEquals("Hello".toCharArray(), parser.getTextCharacters());
 parser.close();
 }

 @Test
 public void testGetTextLength() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree("Hello"));
 assertEquals(5, parser.getTextLength());
 parser.close();
 }

 @Test
 public void testGetTextOffset() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree("Hello"));
 assertEquals(0, parser.getTextOffset());
 parser.close();
 }

 @Test
 public void testHasTextCharacters() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree("Hello"));
 assertFalse(parser.hasTextCharacters());
 parser.close();
 }

 @Test
 public void testGetNumberType() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123L));
 assertEquals(JsonType.NUMBER, parser.getNumberType());
 parser.close();
 }

 @Test
 public void testGetBigIntegerValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(BigInteger.valueOf(123)));
 assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());
 parser.close();
 }

 @Test
 public void testGetDecimalValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(new BigDecimal("123.456")));
 assertEquals(new BigDecimal("123.456"), parser.getDecimalValue());
 parser.close();
 }

 @Test
 public void testGetDoubleValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123.456));
 assertEquals(123.456, parser.getDoubleValue(), 0.001);
 parser.close();
 }

 @Test
 public void testGetFloatValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123.456f));
 assertEquals(123.456f, parser.getFloatValue(), 0.001f);
 parser.close();
 }

 @Test
 public void testGetIntValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123));
 assertEquals(123, parser.getIntValue());
 parser.close();
 }

 @Test
 public void testGetLongValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123L));
 assertEquals(123L, parser.getLongValue());
 parser.close();
 }

 @Test
 public void testGetNumberValue() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(123L));
 assertEquals(123L, parser.getNumberValue().longValue());
 parser.close();
 }

 @Test
 public void testGetEmbeddedObject() throws IOException {
 parser = new TreeTraversingParser(node);
 assertEquals(node.get("skills"), parser.getEmbeddedObject());
 parser.close();
 }

 @Test
 public void testIsNaN() throws IOException {
 parser = new TreeTraversingParser(mapper.valueToTree(Double.NaN));
 assertTrue(parser.isNaN());
 parser.close();
 }

 @Test
 public void testGetVersion() throws IOException {
 parser = new TreeTraversingParser(node);
 assertEquals(PackageVersion.VERSION, parser.version());
 parser.close();
 }
}