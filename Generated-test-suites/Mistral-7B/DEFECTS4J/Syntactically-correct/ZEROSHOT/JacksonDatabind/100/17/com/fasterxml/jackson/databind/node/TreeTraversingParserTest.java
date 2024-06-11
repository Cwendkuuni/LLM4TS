package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private TreeTraversingParser parser;
    private JsonNode rootNode;

    @Before
    public void setup() throws JsonProcessingException {
        jsonFactory = new ObjectMapper().getFactory();
        rootNode = jsonFactory.createParser("{\"foo\":1,\"bar\":[\"apples\",1.5],\"baz\":{\"quux\":\"hello\"}}")
                .readValueAsTree();
        objectMapper = new ObjectMapper();
        parser = new TreeTraversingParser(rootNode);
    }

    @Test
    public void testVersion() throws JsonProcessingException {
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testSetCodec() throws JsonProcessingException {
        ObjectCodec codec = objectMapper.getFactory().getCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testGetCodec() throws JsonProcessingException {
        assertEquals(objectMapper.getFactory().getCodec(), parser.getCodec());
    }

    @Test
    public void testIsClosed() throws JsonProcessingException {
        assertFalse(parser.isClosed());

        parser.close();
        assertTrue(parser.isClosed());

        parser = new TreeTraversingParser(rootNode);
        assertFalse(parser.isClosed());
    }

    @Test
    public void testNextToken() throws JsonProcessingException {
        JsonToken expectedToken = JsonToken.START_OBJECT;
        JsonToken actualToken;

        parser.nextToken();
        actualToken = parser.getCurrentToken();
        assertEquals(expectedToken, actualToken);
        expectedToken = parser.nextToken().getCurrentToken();

        assertSame(expectedToken, parser.getCurrentToken());
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testSkipChildren() throws JsonProcessingException {
        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());

        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());

        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getCurrentToken());

        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());

        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());

        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());

        parser.nextToken();
        parser.skipChildren();
        assertNull(parser.getCurrentToken());
    }

    @Test
    public void testClose() throws JsonProcessingException {
        parser.nextToken();
        parser.nextToken();
        parser.close();
        parser.nextToken();
        assertNull(parser.getCurrentToken());
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterClose() throws JsonProcessingException {
        parser.nextToken();
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testGetText() throws JsonProcessingException {
        assertEquals("foo", parser.getText());
        parser.nextToken();
        assertEquals("apples", parser.getText());
        parser.nextToken();
        assertEquals("1.5", parser.getText());
        parser.nextToken();
        assertEquals("quux", parser.getText());
        parser.nextToken();
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws JsonProcessingException {
        char[] expectedCharacters = "foo".toCharArray();
        char[] actualCharacters = parser.getTextCharacters();
        assertArrayEquals(expectedCharacters, actualCharacters);

        parser.nextToken();
        expectedCharacters = "apples".toCharArray();
        actualCharacters = parser.getTextCharacters();
        assertArrayEquals(expectedCharacters, actualCharacters);

        parser.nextToken();
        expectedCharacters = "1.5".toCharArray();
        actualCharacters = parser.getTextCharacters();
        assertArrayEquals(expectedCharacters, actualCharacters);

        parser.nextToken();
        expectedCharacters = "quux".toCharArray();
        actualCharacters = parser.getTextCharacters();
        assertArrayEquals(expectedCharacters, actualCharacters);

        parser.nextToken();
        assertArrayEquals(new char[0], parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws JsonProcessingException {
        assertEquals(3, parser.getTextLength());
        parser.nextToken();
        assertEquals(6, parser.getTextLength());
        parser.nextToken();
        assertEquals(3, parser.getTextLength());
        parser.nextToken();
        assertEquals(5, parser.getTextLength());
        parser.nextToken();
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws JsonProcessingException {
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws JsonProcessingException {
        assertFalse(parser.hasTextCharacters());
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws JsonProcessingException {
        assertEquals(JsonType.NUMBER, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonType.STRING, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonType.INTEGER, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonType.NUMBER, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonType.NUMBER, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonType.NUMBER, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonType.NULL, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws JsonProcessingException {
        BigInteger expectedValue = BigInteger.valueOf(1);
        BigInteger actualValue = parser.getBigIntegerValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        expectedValue = BigInteger.valueOf(42);
        actualValue = parser.getBigIntegerValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws JsonProcessingException {
        BigDecimal expectedValue = new BigDecimal("1.0");
        BigDecimal actualValue = parser.getDecimalValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        expectedValue = new BigDecimal("42.0");
        actualValue = parser.getDecimalValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws JsonProcessingException {
        double expectedValue = 1.0;
        double actualValue = parser.getDoubleValue();
        assertEquals(expectedValue, actualValue, 0.0);

        parser.nextToken();
        expectedValue = 42.0;
        actualValue = parser.getDoubleValue();
        assertEquals(expectedValue, actualValue, 0.0);

        parser.nextToken();
        assertNull(parser.getDoubleValue());
    }

    @Test
    public void testGetFloatValue() throws JsonProcessingException {
        float expectedValue = 1.0f;
        float actualValue = parser.getFloatValue();
        assertEquals(expectedValue, actualValue, 0.0f);

        parser.nextToken();
        expectedValue = 42.0f;
        actualValue = parser.getFloatValue();
        assertEquals(expectedValue, actualValue, 0.0f);

        parser.nextToken();
        assertNull(parser.getFloatValue());
    }

    @Test
    public void testGetLongValue() throws JsonProcessingException {
        long expectedValue = 1;
        long actualValue = parser.getIntValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        expectedValue = 42;
        actualValue = parser.getIntValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        assertNull(parser.getIntValue());
    }

    @Test
    public void testGetIntValue() throws JsonProcessingException {
        int expectedValue = 1;
        int actualValue = parser.getIntValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        expectedValue = 42;
        actualValue = parser.getIntValue();
        assertEquals(expectedValue, actualValue);

        parser.nextToken();
        assertNull(parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws JsonProcessingException {
        Number expectedValue = 1;
        Number actualValue = parser.getNumberValue();
        assertEquals(expectedValue.doubleValue(), actualValue.doubleValue(), 0.0);

        parser.nextToken();
        expectedValue = 42;
        actualValue = parser.getNumberValue();
        assertEquals(expectedValue.doubleValue(), actualValue.doubleValue(), 0.0);

        parser.nextToken();
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws JsonProcessingException {
        Object expectedObject = new Object();
        Object actualObject = parser.getEmbeddedObject();
        assertEquals(expectedObject, actualObject);

        parser.nextToken();
        parser.nextToken();
        expectedObject = new ArrayNode(jsonFactory);
        actualObject = parser.getEmbeddedObject();
        assertEquals(expectedObject.getClass(), actualObject.getClass());
        assertEquals(3, ((ArrayNode) actualObject).size());

        parser.nextToken();
        parser.nextToken();
        expectedObject = new ObjectNode(jsonFactory);
        expectedObject.put("quux", new TextNode("hello"));
        actualObject = parser.getEmbeddedObject();
        assertEquals(expectedObject.getClass(), actualObject.getClass());
        assertEquals("hello", ((TextNode) expectedObject.get("quux")).asText());
        assertEquals("hello", ((TextNode) ((ObjectNode) actualObject).get("quux")).asText());

        parser.nextToken();
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws JsonProcessingException {
        assertFalse(parser.isNaN());
        parser.nextToken();
        JsonNode currentNode = parser.currentNode();
        currentNode.setDouble(Double.NaN);
        assertTrue(parser.isNaN());
        parser.nextToken();
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws JsonProcessingException {
        byte[] expectedValue = "Hello World".getBytes();
        byte[] actualValue = parser.getBinaryValue(Base64Variant.DEFAULT);
        assertArrayEquals(expectedValue, actualValue);

        parser.nextToken();
        parser.nextToken();
        expectedValue = "Rosetta Stone".getBytes();
        actualValue = parser.getBinaryValue(Base64Variant.DEFAULT);
        assertArrayEquals(expectedValue, actualValue);

        parser.nextToken();
        assertNull(parser.getBinaryValue(Base64Variant.DEFAULT));
    }

    @Test
    public void testReadBinaryValue() throws JsonProcessingException {
        byte[] expectedValue = "Hello World".getBytes();
        byte[] actualValue = new byte[expectedValue.length];

        parser.nextToken();
        parser.nextToken();
        int length = parser.readBinaryValue(Base64Variant.DEFAULT, new ByteArrayOutputStream(expectedValue.length));
        parser.readFully(expectedValue, 0, expectedValue.length);
        parser.close();

        assertEquals(length, expectedValue.length);
        assertArrayEquals(expectedValue, actualValue);

        parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        length = parser.readBinaryValue(Base64Variant.DEFAULT, new ByteArrayOutputStream(expectedValue.length));
        parser.readFully(expectedValue, 0, expectedValue.length);
        parser.close();

        parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        length = parser.readBinaryValue(Base64Variant.DEFAULT, new ByteArrayOutputStream(expectedValue.length));
        parser.close();

        assertNull(parser.getBinaryValue(Base64Variant.DEFAULT));
    }

    @Test
    public void testCurrentNode() throws JsonProcessingException {
        JsonNode expectedNode = parser.currentNode();
        JsonNode actualNode = parser.getCurrentNode();
        assertEquals(expectedNode, actualNode);

        parser.nextToken();
        expectedNode = parser.getCurrentNode();
        actualNode = parser.currentNode();
        assertEquals(expectedNode, actualNode);

        parser.nextToken();
        expectedNode = parser.getCurrentNode();
        actualNode = parser.currentNode();
        assertEquals(expectedNode, actualNode);

        parser.nextToken();
        expectedNode = null;
        actualNode = parser.currentNode();
        assertNull(actualNode);
    }

    @Test
    public void testCurrentNumericNode() throws JsonProcessingException {
        JsonNode expectedNode = parser.currentNumericNode();
        JsonNode actualNode = parser.currentNode();
        if (actualNode instanceof NumericNode) {
            expectedNode = actualNode;
        }
        assertEquals(expectedNode, parser.currentNumericNode());

        parser.nextToken();
        expectedNode = parser.currentNode();
        if (expectedNode instanceof NumericNode) {
            actualNode = parser.currentNumericNode();
            assertEquals(expectedNode, actualNode);
        }
        parser.nextToken();
        assertEquals(parser.currentNumericNode(), null);
    }

}