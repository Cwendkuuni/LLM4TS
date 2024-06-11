package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @org.junit.Test
    public void testEmptyTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new Object()));
        JsonToken token = parser.nextToken();
        assertNull(token);
        parser.close();
    }

    @org.junit.Test
    public void testSingleValueTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("Hello, World!"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Hello, World!", parser.getText());
        parser.close();
    }

    @org.junit.Test
    public void testNullValueTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(null));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, token);
        parser.close();
    }

    @org.junit.Test
    public void testArrayTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new int[] { 1, 2, 3 }));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        for (int i = 0; i < 3; i++) {
            token = parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, token);
            assertEquals(Integer.toString(i + 1), parser.getText());
        }

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @org.junit.Test
    public void testArrayWithNullValueTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new int[] { 1, null, 3 }));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        for (int i = 0; i < 3; i++) {
            if (i == 1) {
                token = parser.nextToken();
                assertEquals(JsonToken.VALUE_NULL, token);
            } else {
                token = parser.nextToken();
                assertEquals(JsonToken.VALUE_NUMBER_INT, token);
                assertEquals(Integer.toString(i + 1), parser.getText());
            }
        }

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @org.junit.Test
    public void testObjectTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("{\"name\":\"John\", \"age\":30}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // value: "John"

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // value: 30

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @org.junit.Test
    public void testObjectWithNullValueTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("{\"name\":\"John\", \"age\":null}"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // value: "John"

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @org.junit.Test
    public void testNumericTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(123));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals("123", parser.getText());
        assertEquals(Integer.TYPE, parser.getNumberType());
        assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());
        assertEquals(new BigDecimal("123"), parser.getDecimalValue());
        assertEquals(123.0, parser.getDoubleValue(), 0.0);
        assertEquals(123.0f, parser.getFloatValue(), 0.0);
        assertEquals(123, parser.getIntValue());
        assertEquals(123L, parser.getLongValue());
        assertEquals(123.0, parser.getNumberValue().doubleValue(), 0.0);

        parser.close();
    }

    @org.junit.Test
    public void testNumericTreeWithFloatValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(123.5f));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals("123.5", parser.getText());
        assertEquals(Float.TYPE, parser.getNumberType());
        assertEquals(new Float(123.5f), parser.getNumberValue());
        assertEquals(123.5f, parser.getFloatValue(), 0.0);

        parser.close();
    }

    @org.junit.Test
    public void testNumericTreeWithBigDecimalValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new BigDecimal("123.456789")));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals("123.456789", parser.getText());
        assertEquals(BigDecimal.TYPE, parser.getNumberType());
        assertEquals(parser.getNumberValue(), new BigDecimal("123.456789"));

        parser.close();
    }

    @org.junit.Test
    public void testNumericTreeWithBigIntegerValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(BigInteger.valueOf(12345678901234567890L)));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals("12345678901234567890", parser.getText());
        assertEquals(BigInteger.TYPE, parser.getNumberType());
        assertEquals(parser.getNumberValue().longValueExact(), 12345678901234567890L);

        parser.close();
    }

    @org.junit.Test
    public void testNumericTreeWithNaNValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(Double.NaN));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertTrue(parser.isNaN());

        parser.close();
    }

    @org.junit.Test
    public void testTextTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("Hello, World!"));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Hello, World!", parser.getText());

        parser.close();
    }

    @org.junit.Test
    public void testBinaryTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new byte[] { 0x01, 0x02, 0x03 }));
        byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertEquals("RgphdmFrcm91dA==", new String(binaryData, "US-ASCII"));

        int bytesRead = parser.readBinaryValue(Base64Variant.BASE64_STANDARD, new byte[3]);
        assertEquals(3, bytesRead);

        parser.close();
    }

    @org.junit.Test
    public void testCloseParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(123));
        parser.close();

        JsonToken token = parser.nextToken();
        assertFalse(parser.isOpen());
        assertNull(token);
    }

    @org.junit.Test(expected = JsonParseException.class)
    public void testEmptyContainerTree() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new ObjectNode(new Object())));
        parser.nextToken();
        parser.skipChildren();
        parser.nextToken();
        parser.nextToken(); // should throw an exception
    }

    @org.junit.Test
    public void testNestedContainerTree() throws IOException {
        JsonNode rootNode = JsonNode.valueOf(new Object() {
            Object obj1 = new Object() {
                int arr1[] = { 1, 2 };
            };
            Object obj2 = new Object() {
                int arr2[] = { 3, 4 };
            };
        });

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("obj1", parser.getCurrentName());
        parser.nextToken(); // value: "{}"

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("arr1", parser.getCurrentName());
        parser.nextToken(); // value: "[1, 2]"

        parser.skipChildren();

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("obj2", parser.getCurrentName());
        parser.nextToken(); // value: "{}"

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("arr2", parser.getCurrentName());
        parser.nextToken(); // value: "[3, 4]"

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @org.junit.Test
    public void testCurrentLocation() throws IOException {
        JsonNode rootNode = JsonNode.valueOf(new Object() {
            Object obj = new Object() {
                int arr[] = { 1, 2 };
            };
        });

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        JsonLocation location = parser.getParsingContext();
        JsonLocation currentLocation = parser.getCurrentLocation();

        parser.nextToken(); // root
        parser.nextToken(); // obj
        parser.nextToken(); // arr
        parser.nextToken(); // 1

        assertEquals(location, parser.getParsingContext());
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.nextToken(); // 2

        assertEquals(location, parser.getParsingContext());
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.skipChildren();

        parser.nextToken(); // obj
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.nextToken(); // arr
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.nextToken(); // 1
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.nextToken(); // obj
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.nextToken(); // arr
        assertEquals(currentLocation, parser.getCurrentLocation());

        parser.nextToken(); // 2

        parser.close();
    }
}