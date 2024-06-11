package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;

/**
 * Test class for {@link TreeTraversingParser} class.
 */
public class TreeTraversingParserTest {

    @Test
    public void testBasicParsing() throws JsonParseException, IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Test nextToken method
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());
        assertEquals("30", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("city", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("New York", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren method
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed method
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testClosedParser() throws JsonParseException, IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testBinaryParsing() throws JsonParseException, IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\", \"binary\":\"Rm9udCAoKTs=\", \"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Test getBinaryValue method
        byte[] binaryData = parser.getBinaryValue(Base64Variant.STANDARD);
        assertNotNull(binaryData);
        assertEquals(13, binaryData.length);

        // Test readBinaryValue method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = parser.readBinaryValue(Base64Variant.STANDARD, outputStream);
        assertEquals(13, read);
        byte[] readBinaryData = outputStream.toByteArray();
        assertArrayEquals(binaryData, readBinaryData);

        // Test close method
        parser.close();
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNumericParsing() throws JsonParseException, IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        JsonNode jsonNode = new ObjectMapper().readTree("{\"number\":1.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Test getNumberValue method
        Number number = parser.getNumberValue();
        assertNotNull(number);
        assertEquals(1.5, number.doubleValue());

        // Test getNumberType method
        NumberType numberType = parser.getNumberType();
        assertEquals(NumberType.FLOAT, numberType);

        // Test getIntValue method
        int intValue = parser.getIntValue();
        assertEquals(1, intValue);

        // Test getLongValue method
        long longValue = parser.getLongValue();
        assertEquals(1, longValue);

        // Test getDoubleValue method
        double doubleValue = parser.getDoubleValue();
        assertEquals(1.5, doubleValue, 0.001);

        // Test getFloatValue method
        float floatValue = parser.getFloatValue();
        assertEquals((float) 1.5, floatValue, 0.001f);

        // Test getBigDecimalValue method (not implemented in the given TreeTraversingParser)
        // JsonDecimalNumber jsonDecimalNumber = parser.getDecimalValue();
        // assertEquals(new BigDecimal("1.5"), jsonDecimalNumber);

        // Test getBigIntegerValue method (not implemented in the given TreeTraversingParser)
        // BigInteger bigIntegerValue = parser.getBigIntegerValue();
        // assertEquals(BigInteger.ONE, bigIntegerValue);

        // Test getNaN method
        assertFalse(parser.isNaN());

        // Test close method
        parser.close();
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNullParsing() throws JsonParseException, IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        JsonNode jsonNode = new ObjectMapper().readTree("{\"number\":null}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Test getNumberValue method
        assertNull(parser.getNumberValue());

        // Test getNumberType method
        assertNull(parser.getNumberType());

        // Test getIntValue method
        assertEquals(0, parser.getIntValue());

        // Test getLongValue method
        assertEquals(0L, parser.getLongValue());

        // Test getDoubleValue method
        assertEquals(0.0, parser.getDoubleValue(), 0.001);

        // Test getFloatValue method
        assertEquals(0.0f, parser.getFloatValue(), 0.001f);

        // Test getText method
        assertEquals(null, parser.getText());

        // Test isNaN method
        assertFalse(parser.isNaN());

        // Test close method
        parser.close();
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testEmptyParsing() throws JsonParseException, IOException {
        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        JsonNode jsonNode = new ObjectMapper().readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Test nextToken method
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren method
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed method
        parser.close();
        assertTrue(parser.isClosed());

        // Test getText method
        assertEquals(null, parser.getText());

        // Test getTextCharacters method
        assertNull(parser.getTextCharacters());

        // Test getTextLength method
        assertEquals(0, parser.getTextLength());

        // Test getTextOffset method
        assertEquals(0, parser.getTextOffset());

        // Test hasTextCharacters method
        assertFalse(parser.hasTextCharacters());

        // Test getNumberType method
        assertNull(parser.getNumberType());

        // Test getNumberValue method
        assertNull(parser.getNumberValue());

        // Test getIntValue method
        assertEquals(0, parser.getIntValue());

        // Test getLongValue method
        assertEquals(0L, parser.getLongValue());

        // Test getDoubleValue method
        assertEquals(0.0, parser.getDoubleValue(), 0.001);

        // Test getFloatValue method
        assertEquals(0.0f, parser.getFloatValue(), 0.001f);

        // Test getBigDecimalValue method (not implemented in the given TreeTraversingParser)
        // BigDecimal bigDecimalValue = parser.getDecimalValue();
        // assertNull(bigDecimalValue);

        // Test getBigIntegerValue method (not implemented in the given TreeTraversingParser)
        // BigInteger bigIntegerValue = parser.getBigIntegerValue();
        // assertNull(bigIntegerValue);

        // Test getEmbeddedObject method
        assertNull(parser.getEmbeddedObject());

        // Test isNaN method
        assertFalse(parser.isNaN());

        // Test getCurrentName method
        assertEquals(null, parser.getCurrentName());

        // Test getParsingContext method
        assertEquals(JsonLocation.NA, parser.getParsingContext());

        // Test getTokenLocation method
        assertEquals(JsonLocation.NA, parser.getTokenLocation());

        // Test getCurrentLocation method
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }
}