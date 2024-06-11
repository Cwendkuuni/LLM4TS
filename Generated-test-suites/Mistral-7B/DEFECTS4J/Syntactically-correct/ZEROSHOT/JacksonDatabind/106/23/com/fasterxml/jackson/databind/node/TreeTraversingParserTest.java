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
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TreeTraversingParser.
 */
public class TreeTraversingParserTest {

    private ObjectCodec objectCodec;
    private JsonParser parser;
    private JsonNode testNode;

    @Before
    public void setUp() throws IOException {
        objectCodec = mock(ObjectCodec.class);
        testNode = createTestNode();
        parser = new TreeTraversingParser(testNode, objectCodec);
    }

    @After
    public void tearDown() throws IOException {
        parser.close();
    }

    @Test
    public void testVersion() throws IOException {
        assertEquals(PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec newCodec = mock(ObjectCodec.class);
        parser.setCodec(newCodec);
        assertSame(newCodec, parser.getCodec());
    }

    @Test
    public void testGetCodec() throws IOException {
        assertEquals(objectCodec, parser.getCodec());
    }

    @Test
    public void testClose() throws IOException {
        parser.close();
        assertNull(parser.getCodec());
        assertNull(parser._nodeCursor);
        assertNull(parser._currToken);
    }

    @Test(expected = IOException.class)
    public void testNegativeNextToken() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // should throw an IOException
    }

    @Test
    public void testNextTokenStartArray() throws IOException {
        parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
    }

    @Test
    public void testNextTokenEndArray() throws IOException {
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
    }

    @Test
    public void testNextTokenStartObject() throws IOException {
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testNextTokenEndObject() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testNextTokenValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
    }

    @Test
    public void testNextTokenNull() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertNull(parser.getCurrentToken());
    }

    @Test
    public void testSkipChildrenStartArray() throws IOException {
        parser.nextToken();
        parser.skipChildren();
        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
    }

    @Test
    public void testSkipChildrenStartObject() throws IOException {
        parser.nextToken();
        parser.skipChildren();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testIsNotClosed() throws IOException {
        assertFalse(parser.isClosed());
    }

    @Test
    public void testGetText() throws IOException {
        assertEquals("test", parser.getText());
    }

    @Test
    public void testgetTextCharacters() throws IOException {
        char[] expectedCharArray = "test".toCharArray();
        assertArrayEquals(expectedCharArray, parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException {
        assertEquals(5, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException {
        assertEquals(0, parser.getTextOffset());
    }

    @Test(expected = IOException.class)
    public void testGetTextWhenClosed() throws IOException {
        parser.close();
        parser.getText();
    }

    @Test
    public void testGetNumberType() throws IOException {
        assertEquals(JsonType.NUMBER, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        assertEquals(new BigInteger("12345678901234567890"), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        assertEquals(123.45, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals((float) 123.45, parser.getFloatValue(), 0.001);
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(123, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(1234567890123456789L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        assertEquals(new BigDecimal("123.45"), parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObjectNull() throws IOException {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        JsonNode embeddedNode = createTestNode();
        when(testNode.get(0)).thenReturn(embeddedNode);
        Object embeddedObject = parser.getEmbeddedObject();
        assertEquals(embeddedNode, embeddedObject);
    }

    @Test
    public void testIsNaN() throws IOException {
        JsonNode nanNode = createTestNode("NaN");
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        byte[] expectedValue = new byte[]{0x01, 0x02, 0x03};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = parser.readBinaryValue(Base64Variant.DEFAULT, outputStream);
        byte[] value = outputStream.toByteArray();
        assertArrayEquals(expectedValue, value);
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        byte[] expectedValue = new byte[]{0x01, 0x02, 0x03};
        int read = parser.readBinaryValue(Base64Variant.DEFAULT, new ByteArrayOutputStream());
        assertEquals(expectedValue.length, read);
    }

    @Test(expected = JsonParseException.class)
    public void testGetBinaryValueNull() throws IOException {
        parser.readBinaryValue(Base64Variant.DEFAULT, new ByteArrayOutputStream());
    }

    private JsonNode createTestNode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("{\"test\": \"test\"}");
    }

    private JsonNode createTestNode(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}